package com.pos.base.ui.component;

import com.pos.inventoryfeature.dao.DrugDosageForm;
import com.pos.inventoryfeature.service.provider.DrugDosageFormDataProvider;
import com.pos.retailfeature.dto.ProductDto;
import com.pos.retailfeature.dto.ViewGenericProductDto;
import com.pos.retailfeature.service.providers.GenericProductDataProvider;
import com.pos.retailfeature.service.providers.ProductDataProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.FieldSet;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@UIScope
@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class CreateProductView extends Dialog {
    // id,name,description,generic_id,dosage_form,strength,current_selling_price
    ProgressBar viewProgressBar = new ProgressBar();

    private final String modalDescText = "Fill the form below and click submit to create a new product in the store.";
    private final ProductDataProvider productDataProvider;
    private final GenericProductDataProvider genericProductDataProvider;
    private final DrugDosageFormDataProvider dosageFormDataProvider;

    private final Checkbox closeAfterSaveChkbx = new Checkbox("Close after save", true);
    private final TextField nameTxt = new TextField("Provide Name");
    private final TextArea descriptionTxt = new TextArea("Provide Description (Optional)");
    private final ComboBox<ViewGenericProductDto> genericsCbx = new ComboBox<>("Select Product Type");
    private final ComboBox<DrugDosageForm> dosageFormCbx = new ComboBox<>("Select Dosage Form");
    private final TextField strengthTxt = new TextField("Strength");
    private final NumberField currentSellingPriceNf = new NumberField("Provide Selling Price");
    private final Checkbox isPharmaceuticalChkbx = new Checkbox("Is Pharmaceutical?", false);
    private final Button createBtn = new Button("Create Product");
    private final Button exitBtn = new Button("Exit View");
    private final FieldSet fieldSetPharma = new FieldSet("Pharmaceutical Information");

    @PostConstruct
    private void init() {
        setHeaderTitle("Create Product");
        setWidth("40vw");

        nameTxt.setWidthFull();

        fieldSetPharma.setVisible(false);

        descriptionTxt.setWidthFull();
        genericsCbx.setWidthFull();
        dosageFormCbx.setWidthFull();
        strengthTxt.setWidthFull();

        currentSellingPriceNf.setWidthFull();

        isPharmaceuticalChkbx.setWidthFull();

        fieldSetPharma.add(dosageFormCbx, strengthTxt);
        fieldSetPharma.setWidthFull();

        createBtn.setPrefixComponent(VaadinIcon.HARDDRIVE_O.create());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        exitBtn.setPrefixComponent(VaadinIcon.CLOSE.create());
        exitBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout modalActions = new HorizontalLayout(createBtn, exitBtn);
        modalActions.setWidthFull();
        modalActions.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        Span description = new Span(modalDescText);

        add(viewProgressBar);
        add(description, closeAfterSaveChkbx);
        add(genericsCbx, nameTxt, descriptionTxt);
        add(isPharmaceuticalChkbx, fieldSetPharma);
        add(modalActions);

        setupControls();
        setupGenericCombobox();
        setupDosageFormCombobox();
    }

    void setupControls() {
        // checkbox
        isPharmaceuticalChkbx
                .addValueChangeListener(changeEvent -> toggleElementActiveness(changeEvent.getValue(), fieldSetPharma));
        // buttons
        createBtn.addClickListener(event -> {
            String productName = nameTxt.getValue();
            DrugDosageForm dosageForm = dosageFormCbx.getValue();
            ViewGenericProductDto genericProductId = genericsCbx.getValue();
            String strength = strengthTxt.getValue();

            if (productName.isBlank()) {
                createReportError("Please enter the product name.").open();
                return;
            }

            if (Boolean.TRUE.equals(isPharmaceuticalChkbx.getValue())) {

                if (genericProductId == null) {
                    createReportError("Please select generic product to which this product belong.").open();
                    return;
                }
                if (dosageForm == null) {
                    createReportError("Please select product dosage form.").open();
                    return;
                }
                if (strength.isBlank()) {
                    createReportError("Please provide drug strength").open();
                    return;
                }
            }

            ProductDto productDataDto = new ProductDto(
                    productName,
                    descriptionTxt.getValue(),
                    genericProductId.getId(),
                    dosageForm != null ? dosageForm.getName() : null,
                    strength,
                    BigDecimal
                            .valueOf(currentSellingPriceNf.getValue() != null ? currentSellingPriceNf.getValue() : 0L));
            log.info("To store {}", productDataDto.toString());
            viewProgressBar.setIndeterminate(true);
            saveProduct(productDataDto);
        });
        exitBtn.addClickListener(event -> this.close());
    }

    void setupGenericCombobox() {
        genericsCbx.setItemLabelGenerator(ViewGenericProductDto::getName);
        genericsCbx.setDataProvider(genericProductDataProvider, filterText -> {
            genericProductDataProvider.setFilter(filterText);
            return filterText;
        });
    }

    void setupDosageFormCombobox() {
        dosageFormCbx.setItemLabelGenerator(DrugDosageForm::getName);
        dosageFormCbx.setDataProvider(dosageFormDataProvider, filterTxt -> {
            // dosageFormDataProvider
            return filterTxt;
        });
    }

    void toggleElementActiveness(boolean toggle, Component... elements) {
        for (Component element : elements) {
            element.setVisible(toggle);
        }
    }

    void saveProduct(ProductDto data) {
        productDataProvider.createProduct(data);
        log.info("Create Product Complete {} \n ", data.toString());
        Notification.show("Product (" + data.getName() + ") created successfully", 2000, Notification.Position.MIDDLE);
        if (Boolean.TRUE.equals(closeAfterSaveChkbx.getValue())) {
            close();
        }
        clearElements();
    }

    void clearElements() {
        nameTxt.clear();
        descriptionTxt.clear();
        strengthTxt.clear();
        currentSellingPriceNf.clear();
        dosageFormCbx.clear();
    }

    public Notification createReportError(String message) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        Icon icon = VaadinIcon.WARNING.create();
        Button closeBtn = createCloseBtn();
        var layout = new HorizontalLayout(icon,
                new Text(message),
                closeBtn);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        closeBtn.addClickListener(event -> notification.close());
        notification.add(layout);

        return notification;
    }

    public static Button createCloseBtn() {
        Button closeBtn = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        return closeBtn;
    }

}
