package com.pos.retailfeature.subcomponent;

import com.pos.core.service.CartState;
import com.pos.inventoryfeature.dao.stock.ProductStockSummary;
import com.pos.retailfeature.service.providers.StockDataProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DisplayView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(DisplayView.class);
    private final VirtualList<List<ProductStockSummary>> dataView = new VirtualList<>();
    private final DataProvider<List<ProductStockSummary>, Void> rowProvider;
    private final int columns = 4;
    private final CartState cartState;

    public DisplayView(StockDataProvider dataProvider, CartState cartState) {

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

    private final ComponentRenderer<Component, List<ProductStockSummary>> productRowRenderer =
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


    private Card productCard(ProductStockSummary product) {
        try {
            Card card = new Card();

            Button addToCartBtn = new Button("Add to cart");
            addToCartBtn.setWidthFull();
            addToCartBtn.addClickListener(buttonClickEvent -> {
                ReceiptItem newItem = new ReceiptItem();
                newItem.setProductId(product.productId());
                newItem.setName(product.productName());
                newItem.setQuantity(1);
                newItem.setTotalPrice(product.sellingPrice());
                newItem.setUnitPrice(product.sellingPrice());

                this.cartState.addToCart(newItem);
            });

//            Image image = new Image(
//                    "https://cdn.britannica.com/22/187222-050-07B17FB6/apples-on-a-tree-branch.jpg",
//                    product.productName()
//            );
//            image.setWidthFull();
//
//            card.setMedia(image);
            card.setSubtitle(new Div(product.productName()));

            Div stock = new Div(
                    new H6("$" + product.sellingPrice()),
                    new H6("Stock: " + product.totalQuantity())
            );

            VerticalLayout layout = new VerticalLayout(stock, addToCartBtn);
            layout.setPadding(false);

            card.add(layout);
            card.setWidth("250px");
            card.setMinWidth("250px");

            return card;
        } catch (Exception e) {
            log.error("DisplayView Error {}: {}", product.productName(), e.getMessage());
            return new Card();
        }
    }

    private static DataProvider<List<ProductStockSummary>, Void> rowDataProvider(StockDataProvider source, int columns) {

        try {
            return DataProvider.fromCallbacks(

                    // FETCH
                    query -> {
                        int rowOffset = query.getOffset();
                        int rowLimit = query.getLimit();

                        int productOffset = rowOffset * columns;
                        int productLimit = rowLimit * columns;

                        Query<ProductStockSummary, String> productQuery =
                                new Query<>(productOffset, productLimit, null, null, null);

                        List<ProductStockSummary> products =
                                source.fetch(productQuery).toList();

                        List<List<ProductStockSummary>> rows = new ArrayList<>();
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
        } catch (Exception e) {
            log.error(" display error {}", e.getMessage());
            throw new RuntimeException();
        }

    }


}
