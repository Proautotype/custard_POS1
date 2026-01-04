package com.pos.retailfeature.service.providers;

import com.pos.retailfeature.dao.product.GenericProduct;
import com.pos.retailfeature.dto.CreateGenericProductDto;
import com.pos.retailfeature.dto.ViewGenericProductDto;
import com.pos.retailfeature.service.GenericProductService;
import com.pos.shared.GenericProductMapper;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericProductDataProvider extends AbstractBackEndDataProvider<ViewGenericProductDto,String> {

    private final GenericProductService genericProductService;
    private final GenericProductMapper genericProductMapper;
    @Setter
    private String filter = "";

    public void createGeneric(CreateGenericProductDto createGenericProductDto){
        GenericProduct product = genericProductMapper.toEntity(createGenericProductDto);
        genericProductService.createGeneric(product);
    }

    @Override
    protected Stream<ViewGenericProductDto> fetchFromBackEnd(Query<ViewGenericProductDto, String> query) {
        PageRequest pageable = PageRequest.of(
                query.getOffset() / query.getLimit(),
                query.getLimit()
        );
        List<GenericProduct> genericProducts
                = genericProductService.searchGenerics(pageable);
        return genericProducts.stream().map(genericProductMapper::toDto);
    }

    @Override
    protected int sizeInBackEnd(Query<ViewGenericProductDto, String> query) {
        int size = (int) genericProductService.countFiltered(filter);
        log.info("Count filtered data {} ", size);
        return size;
    }

}
