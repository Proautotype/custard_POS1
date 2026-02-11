package com.pos.inventoryfeature;

import com.pos.base.ui.component.CreateProductView;
import com.pos.inventoryfeature.dao.Purchase;
import com.pos.inventoryfeature.dao.PurchaseRepository;
import com.pos.inventoryfeature.dao.stock.StockEntry;
import com.pos.inventoryfeature.dto.SupplierDto;

import com.pos.inventoryfeature.service.SupplierService;

import com.pos.inventoryfeature.service.provider.SupplierDataProvider;
import com.pos.inventoryfeature.ui.CreateSupplierView;

import com.pos.retailfeature.dto.ProductDto;
import com.pos.retailfeature.service.ProductService;
import com.pos.retailfeature.service.providers.ProductDataProvider;
import com.pos.shared.SpaceBetween;
import com.pos.shared.StockMapper;
import com.pos.shared.Utils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.FieldSet;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

@SpringComponent
@UIScope
@RequiredArgsConstructor
public class StockEntryView extends Dialog {

    private String PURCHASE_ID = "PURCHASE_".concat(Utils.generateIdentifier(8, "_"));
    String batch = Utils.generateIdentifier(8, "_");
    String SUPPLIER_ID = "";
    private final Logger log = LoggerFactory.getLogger(StockEntryView.class);

    private final ProductDataProvider productDataProvider;
    private final CreateProductView createProductDialog;
    private final CreateSupplierView createSupplierViewDialog;

    private final SupplierDataProvider supplierDataProvider;
    private final SupplierService supplierService;
    private final ProductService productService;
    private final PurchaseRepository purchaseRepository;
    private final StockMapper stockMapper;

    private Purchase purchase;

    // views
    ProgressBar viewProgressBar = new ProgressBar();
    ComboBox<SupplierDto> suppliersCbx = new ComboBox<>("Select Supplier");
    Button createSupplierBtn = new Button(VaadinIcon.MALE.create());
    ComboBox<ProductDto> productsCbx = new ComboBox<>("Select Product");
    NumberField currentSellingPriceField = new NumberField("Current Selling Price");
    NumberField quantityReceivedField = new NumberField("Quantity Received");

    TextField batchNumber = new TextField("Batch Number");
    NumberField costPrice = new NumberField("Cost Price");
    TextArea productDescField = new TextArea("Note");
    DatePicker arrivalDate;
    DatePicker expiryDate;

    Grid<StockEntry> stockEntryGrid = new Grid<>(StockEntry.class, false);
    ListDataProvider<StockEntry> stockProvider;

    Button createProductBtn = new Button("Create new product", VaadinIcon.BARCODE.create());
    Button createStockBtn = new Button("Create Stock", VaadinIcon.STOCK.create());
    Button exitBtn = new Button("Exit Stock View", VaadinIcon.EXIT.create());

    final String notice = "Current selling price, if changed will affect the item's selling price throughout the store(application). Thus change it only if selling price has also changed.";
    String supplierInputTxt = "";
    Utils utils = new Utils();

    @PostConstruct
    private void init() {
        setHeaderTitle("Create Stock Entry (" + PURCHASE_ID + ")");
        setTop("Create your stock here, enter each item by filling the form below with it's content. Click the create button to add current item to your stock");
        setWidth("85vw");
        setModal(true);

        createSupplierBtn.setTooltipText("Click here, to create a new supplier");
        createProductBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        quantityReceivedField.setStepButtonsVisible(true);

        createPurchase(PURCHASE_ID);
        viewPurchase(purchase);

        HorizontalLayout actions = new HorizontalLayout();
        createStockBtn.setPrefixComponent(VaadinIcon.ENTER.create());
        createStockBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createStockBtn.setWidth("60%");

        exitBtn.setPrefixComponent(VaadinIcon.CLOSE.create());
        exitBtn.getElement().getThemeList().add("primary error");
        actions.add(createStockBtn, exitBtn);
        actions.setWidthFull();
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        setupButtonActions();
        fieldSetup();
        setupCombobox();

        arrivalDate = new DatePicker("Arrival Date");
        expiryDate = new DatePicker("Expiry Date");

        FormLayout formLayout = new FormLayout();
        formLayout.setAutoResponsive(true);
        formLayout.setColumnWidth("8em");
        formLayout.setExpandColumns(true);
        formLayout.setExpandFields(true);

        formLayout.setAutoResponsive(true);
        formLayout.getElement().appendChild(ElementFactory.createBr());

        formLayout.addFormRow(new SpaceBetween(suppliersCbx, batchNumber));
        formLayout.addFormRow((new SpaceBetween(quantityReceivedField, costPrice)));
        formLayout.addFormRow(productDescField);
        formLayout.addFormRow((new SpaceBetween(arrivalDate, expiryDate)));
        formLayout.getElement().appendChild(ElementFactory.createBr());

        var leftViewActions = nonPaddedLayout(createProductBtn);

        Span noticeTxt = new Span(notice);
        noticeTxt.addClassNames(LumoUtility.Background.TINT_5,
                LumoUtility.TextColor.SECONDARY,
                LumoUtility.FontSize.XXSMALL);
        Details noticeDetail = new Details("Notice", noticeTxt);
        noticeDetail.setOpened(false);
        FieldSet productInfoFieldSet = new FieldSet("Product Information",
                nonPaddedLayoutVertical(
                        nonPaddedLayout(productsCbx, currentSellingPriceField),
                        noticeDetail));
        productInfoFieldSet.setWidthFull();
        productInfoFieldSet.addClassNames(LumoUtility.Gap.MEDIUM);

        FieldSet stockInformationFieldSet = new FieldSet("Stock Entry Information", formLayout);
        stockInformationFieldSet.setWidthFull();

        addClassName(LumoUtility.Gap.MEDIUM);
        VerticalLayout leftArea = new VerticalLayout();
        leftArea.add(leftViewActions, productInfoFieldSet, stockInformationFieldSet, actions);
        leftArea.setHeightFull();
        leftArea.setWidth("49%");
        VerticalLayout rightArea = new VerticalLayout();
        rightArea.add(dataview());
        rightArea.setWidthFull();
        rightArea.setHeightFull();
        rightArea.setPadding(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout(leftArea, rightArea);
        horizontalLayout.setWidthFull();

        Div mainContent = new Div();
        mainContent.add(viewProgressBar, horizontalLayout);
        mainContent.setWidthFull();
        mainContent.setHeightFull();

        add(mainContent, createProductDialog, createSupplierViewDialog);
        setupCreateProductDialog();

    }

    private void viewPurchase(Purchase purchase2) {

    }

    private VerticalLayout dataview() {
        stockEntryGrid.setHeightFull();

        stockEntryGrid.addColumn(StockEntry::getBatchNumber).setHeader("Batch Number");
        stockEntryGrid.addColumn(args -> args.getProduct().getName()).setHeader("Product");
        stockEntryGrid.addColumn(StockEntry::getQuantityReceived).setHeader("Quantity Received");
        stockEntryGrid.addColumn(StockEntry::getCostPrice).setHeader("Cost Price");
        stockEntryGrid.addColumn(StockEntry::getArrivalDate).setHeader("Arrival Date");
        stockEntryGrid.addColumn(StockEntry::getExpiryDate).setHeader("Expiry Date");

        GridContextMenu<StockEntry> menu = stockEntryGrid.addContextMenu();
        menu.addSeparator();
        menu.addItem(utils.visualComponent("View", VaadinIcon.EYE),
                event -> {
                });

        VerticalLayout contentLayout = new VerticalLayout();

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.add(new H4("Total"));
        headerLayout.getStyle().setBoxShadow("rgba(0, 0, 0, 0.16) 0px 1px 4px").setWidth("100%").setPadding("10px");

        contentLayout.add(stockEntryGrid, createProductDialog);

        return contentLayout;
    }

    private void setupCombobox() {
        productsCbx.setItemLabelGenerator(ProductDto::getName);
        productsCbx.setDataProvider(
                productDataProvider,
                filterText -> {
                    productDataProvider.setFilter(filterText);
                    return filterText;
                });
        productsCbx.addValueChangeListener((comboBoxProductDtoComponentValueChangeEvent) -> {
            ProductDto value = comboBoxProductDtoComponentValueChangeEvent.getValue();
            if (value != null) {
                String searchText = String.valueOf(value.getSellingPrice());
                currentSellingPriceField.setValue(value.getSellingPrice().doubleValue());
            } else {
                currentSellingPriceField.setValue(0.0);
            }

        });

        suppliersCbx.setAllowCustomValue(true);
        suppliersCbx.setItemLabelGenerator(SupplierDto::getName);
        suppliersCbx.setDataProvider(supplierDataProvider, s -> {
            supplierDataProvider.filter = s;
            return s;
        });
        suppliersCbx.addCustomValueSetListener(event -> {
            supplierInputTxt = event.getDetail();
            log.info("inputText {} ", supplierInputTxt);
        });
        suppliersCbx.addValueChangeListener(event -> {
            SupplierDto supplier = event.getValue();
            SUPPLIER_ID = supplier != null ? supplier.getId() : "";
        });

        suppliersCbx.addBlurListener(comboBoxBlurEvent -> {
            ComboBox<SupplierDto> source = comboBoxBlurEvent.getSource();

            SupplierDto sourceValue = source.getValue();
            if (sourceValue == null) {

                String inputText = source.getElement().getText();

                log.info("inputText {}  ", inputText);

                Notification.show("No supplier selected; ");
                createSupplierViewDialog.setName(supplierInputTxt);
                createSupplierViewDialog.open();
            }
        });
    }

    DomEventListener domEventListener = domEvent -> {
        log.info("Dom changed!!! ");
        log.info("eventData{}\n", domEvent.getEventData());
        log.info("Type{}\n", domEvent.getType());
        log.info("Text{}\n", domEvent.getSource().getText());
        log.info("TextStr value {}\n", domEvent.getSource().getProperty("value"));
    };

    void setupButtonActions() {
        createProductBtn.addClickListener(event -> {
            createProductDialog.open();
        });
        exitBtn.addClickListener(event -> {
            this.close();
        });
        createStockBtn.addClickListener(event -> createStock());
    }

    void setupCreateProductDialog() {
        createProductDialog.setCloseOnOutsideClick(false);
        createProductDialog.setModal(true);
        createProductDialog.add();
    }

    HorizontalLayout nonPaddedLayout(Component... components) {
        HorizontalLayout content = new HorizontalLayout(components);
        content.setWidthFull();
        content.setPadding(false);
        content.setMargin(false);
        return content;
    }

    VerticalLayout nonPaddedLayoutVertical(Component... components) {
        VerticalLayout content = new VerticalLayout(components);
        content.setWidthFull();
        content.setPadding(false);
        content.setMargin(false);
        return content;
    }

    void fieldSetup() {
        batchNumber.setValue(batch);
        batchNumber.setEnabled(false);
        quantityReceivedField.setStepButtonsVisible(true);
        quantityReceivedField.setRequired(true);
        quantityReceivedField.setMin(0);
    }

    @Transactional
    void createStock() {

        StockEntryDto stockEntryDto = getStockEntryDto();
        ProductDto productsCbxValue = productsCbx.getValue();
        Double currentSellingPrice = currentSellingPriceField.getValue();

        if (productsCbxValue == null || currentSellingPrice == null) {
            Notification.show("Select Product, it's current selling price cannot be empty");
            return;
        }

        try {
            StockEntry stockEntry = stockMapper.toEntity(stockEntryDto);
            stockEntry.setSupplier(supplierService.findById(stockEntryDto.getSupplierId()).orElseThrow());
            stockEntry.setProduct(productService.findById(productsCbxValue.getId()).orElseThrow());

            purchase.addStockEntry(stockEntry);
            purchaseRepository.save(purchase);
            productService.updateProductSellingPrice(productsCbxValue.getId(), currentSellingPrice);
            stockProvider.refreshAll();
            log.info("stock created successfully {} ", stockEntry.getId());
            Notification.show("Stock created");

        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }

    }

    /**
     * Get purchase from cache, or fetch from database if not cached
     */
    private Purchase createPurchase(String purchaseId) {
        purchase = purchaseRepository.findById(purchaseId).orElseGet(() -> {
            Purchase newPurchase = new Purchase();
            newPurchase.setId(purchaseId);
            return newPurchase;
        });
        stockProvider = new ListDataProvider<>(purchase != null ? purchase.getStockEntries() : new ArrayList<>());
        stockEntryGrid.setDataProvider(stockProvider);
        return purchase;
    }

    Component visualComponent(String name, VaadinIcon icon) {
        Button action = new Button(name, icon.create());
        action.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        return action;
    }

    public void setPurchaseId(String purchaseId) {
        log.info("Setting purchase id to {} ", purchaseId);
        this.PURCHASE_ID = purchaseId;
    }

    StockEntryDto getStockEntryDto() {
        String supplierID = suppliersCbx.getValue().getId();
        String batchNumberValue = batchNumber.getValue();
        int quantityReceivedValue = quantityReceivedField.getValue().intValue();
        String descFieldValue = productDescField.getValue();
        LocalDate arrivalDateValue = arrivalDate.getValue();
        LocalDate expiryDateValue = expiryDate.getValue();

        StockEntryDto stockEntryDto = StockEntryDto.builder()
                .batchNumber(batchNumberValue)
                .costPrice(BigDecimal.valueOf(costPrice.getValue()))
                .quantityReceived(quantityReceivedValue)
                .arrivalDate(arrivalDateValue)
                .expiryDate(expiryDateValue)
                .description(descFieldValue)
                .supplierId(supplierID)
                .build();

        return stockEntryDto;
    }

}
