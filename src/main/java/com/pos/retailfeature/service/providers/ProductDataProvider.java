package com.pos.retailfeature.service.providers;

import com.pos.retailfeature.dto.ProductDto;
import com.pos.retailfeature.service.ProductService;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Slf4j
@Setter
public class ProductDataProvider extends AbstractBackEndDataProvider<ProductDto, String> {

    private final ProductService productService;
    private String filter = "";

    public ProductDataProvider(ProductService productService) {
        this.productService = productService;
    }

    public void createProduct(ProductDto productDto) {
        log.info("provider create product");
        productService.createProduct(productDto);
    }

    @Override
    protected Stream<ProductDto> fetchFromBackEnd(Query<ProductDto, String> query) {
        PageRequest pageable = PageRequest.of(
                query.getOffset() / query.getLimit(),
                query.getLimit()
        );

        return productService.search(filter, pageable)
                .stream().map(ProductDto::toDto);
    }

    @Override
    protected int sizeInBackEnd(Query<ProductDto, String> query) {
        int countFiltered = (int) productService.count(filter);
        log.info("Count filtered data {} ", countFiltered);
        return countFiltered;
    }
}
