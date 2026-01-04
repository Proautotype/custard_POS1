package com.pos.retailfeature.subcomponent;

import com.pos.core.service.CartState;
import com.pos.retailfeature.dto.ProductDto;
import com.pos.retailfeature.service.providers.ProductDataProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DisplayView extends VerticalLayout {

    private final VirtualList<List<ProductDto>> dataView = new VirtualList<>();
    private final DataProvider<List<ProductDto>, Void> rowProvider;
    private int columns = 4;
    private final CartState cartState;

    public DisplayView(ProductDataProvider dataProvider, CartState cartState){

        setSizeFull();

        this.rowProvider = rowDataProvider(dataProvider, columns);
        this.cartState = cartState;

        dataView.setDataProvider(rowProvider);
        dataView.setRenderer(productRowRenderer);
        dataView.setSizeFull();
        expand(dataView);

        add(new H5("Result..."), dataView);
    }

    public void refresh() {
        rowProvider.refreshAll();
    }

    private final ComponentRenderer<Component, List<ProductDto>> productRowRenderer =
            new ComponentRenderer<>(products -> {

                HorizontalLayout row = new HorizontalLayout();
                row.setWidthFull();
                row.setSpacing(true);
                row.getStyle().setMarginTop("5px");
                row.getStyle().setMarginBottom("5px");

                products.forEach(p -> {

                    Card pcard = productCard(p);

                    row.add(pcard);
                });
                return row;
            });


    private Card productCard(ProductDto product) {
        Card card = new Card();

        Button addToCartBtn = new Button("Add to cart");
        addToCartBtn.setWidthFull();
        addToCartBtn.addClickListener(buttonClickEvent ->  {
            ReceiptItem newItem = new ReceiptItem();
            newItem.setName(product.getName());
            newItem.setQuantity(1);
            newItem.setUnitPrice(product.getSellingPrice());
            newItem.setTotalPrice(product.getSellingPrice());
            this.cartState.addToCart(newItem);
        });

        Image image = new Image(
                "https://cdn.britannica.com/22/187222-050-07B17FB6/apples-on-a-tree-branch.jpg",
                product.getName()
        );
        image.setWidthFull();

        card.setMedia(image);
        card.setSubtitle(new Div(product.getName()));

        Div stock = new Div(
                new H6("$" + product.getSellingPrice()),
                new H6("Stock: " + 0)
        );

        VerticalLayout layout = new VerticalLayout(stock, addToCartBtn);
        layout.setPadding(false);

        card.add(layout);
        card.setWidth("250px");
        card.setMinWidth("250px");

        return card;
    }

    private static DataProvider<List<ProductDto>, Void> rowDataProvider(ProductDataProvider source, int columns) {

        return DataProvider.fromCallbacks(

                // FETCH
                query -> {
                    int rowOffset = query.getOffset();
                    int rowLimit = query.getLimit();

                    int productOffset = rowOffset * columns;
                    int productLimit  = rowLimit * columns;

                    Query<ProductDto, String> productQuery =
                            new Query<>(productOffset, productLimit, null, null, null);

                    List<ProductDto> products =
                            source.fetch(productQuery).toList();

                    List<List<ProductDto>> rows = new ArrayList<>();
                    for (int i = 0; i < products.size(); i += columns) {
                        rows.add(products.subList(
                                i,
                                Math.min(i + columns, products.size())
                        ));
                    }
                    return rows.stream();
                },

                // SIZE
                query -> {
                    int totalProducts = source.size(new Query<>());
                    return (int) Math.ceil((double) totalProducts / columns);
                }
        );
    }



}
