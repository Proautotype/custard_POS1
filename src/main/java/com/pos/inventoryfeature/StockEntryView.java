package com.pos.inventoryfeature;

import com.pos.base.ui.component.CreateProductView;
import com.pos.inventoryfeature.service.StockEntryService;
import com.pos.inventoryfeature.service.provider.DrugDosageFormDataProvider;
import com.pos.retailfeature.dto.ProductDto;
import com.pos.retailfeature.service.providers.GenericProductDataProvider;
import com.pos.retailfeature.service.providers.ProductDataProvider;
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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.FieldSet;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
@RequiredArgsConstructor
public class StockEntryView extends Dialog {

    private final ProductDataProvider productDataProvider;
    private final GenericProductDataProvider genericProductDataProvider;
    private final DrugDosageFormDataProvider dosageFormDataProvider;
    private final CreateProductView createProductDialog;

    // **** DI
    private final ObjectProvider<StockEntryService> stockEntryServiceObjectProvider;

    // views
    ProgressBar viewProgressBar = new ProgressBar();
    ComboBox<ProductDto> productsCbx = new ComboBox<>("Select Product");
    TextField existingPriceTxt = new TextField("Current Selling Price");
    NumberField quantityReceived = new NumberField("Quantity Received");
    NumberField productSellingPriceField = new NumberField("Cost Price");
    TextArea productDescField = new TextArea("Note");
    DatePicker arrivalDate;
    DatePicker expiryDate;
    List<StockEntryDto> stocks = new ArrayList<>();
    ListDataProvider<StockEntryDto> listDataProvider;
    Button createProductBtn = new Button("Create new product", VaadinIcon.BARCODE.create());
    Button createStockBtn = new Button("Create Stock", VaadinIcon.STOCK.create());
    Button exitBtn = new Button("Exit Stock View", VaadinIcon.EXIT.create());

    String currentStocksId = Utils.generateIdentifier(8);
    final String notice = "Current selling price, if changed will affect the item's selling price throughout the store(application). Thus change it only if selling price has also changed.";

    @PostConstruct
    private void init () {
        setHeaderTitle("Create Stock Entry (" + currentStocksId + ")");
        setTop("Create your stock here, enter each item by filling the form below with it's content. Click the create button to add current item to your stock");
        setWidth("80vw");
        setModal(true);

        createProductBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        listDataProvider = new ListDataProvider<>(stocks);

        quantityReceived.setStepButtonsVisible(true);

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
        formLayout.addFormRow(productSellingPriceField);
        formLayout.addFormRow(quantityReceived);
        formLayout.addFormRow(productDescField);
        formLayout.addFormRow(nonPaddedLayout(arrivalDate, expiryDate));
        formLayout.getElement().appendChild(ElementFactory.createBr());

        var leftViewActions = nonPaddedLayout(createProductBtn);

        Span noticeTxt = new Span(notice);
        noticeTxt.addClassNames(LumoUtility.Background.TINT_5, LumoUtility.TextColor.SECONDARY);
        Details noticeDetail = new Details("Notice", noticeTxt);
        noticeDetail.setOpened(true);
        FieldSet productInfoFieldSet = new FieldSet("Product Information",
                nonPaddedLayoutVertical(
                        nonPaddedLayout(productsCbx, existingPriceTxt),
                        noticeDetail
                )
        );
        productInfoFieldSet.setWidthFull();
        productInfoFieldSet.addClassNames(LumoUtility.Gap.MEDIUM);

        FieldSet stockInformationFieldSet = new FieldSet("Stock Information", formLayout);
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

        add(mainContent, createProductDialog);

        setupCreateProductDialog();
    }

    private VerticalLayout dataview() {
        Grid<StockEntryDto> stockEntryGrid = new Grid<>();
        stockEntryGrid.setDataProvider(listDataProvider);
        stockEntryGrid.setMultiSort(true);
        stockEntryGrid.setHeightFull();

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
                }
        );
        productsCbx.addValueChangeListener((comboBoxProductDtoComponentValueChangeEvent) -> {
            ProductDto value = comboBoxProductDtoComponentValueChangeEvent.getValue();
            if (value != null) {
                String searchText = String.valueOf(value.getSellingPrice());
                existingPriceTxt.setValue(searchText);
            } else {
                existingPriceTxt.setValue("");
            }

        });
    }

    void setupButtonActions() {
        createProductBtn.addClickListener(event -> {
            createProductDialog.open();
        });
        exitBtn.addClickListener(event -> {
            this.close();
        });
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

    void createStock(StockEntryDto stockEntryDto){

    }

}
