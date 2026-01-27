package com.pos.inventoryfeature.service;

import com.pos.inventoryfeature.dao.Supplier;
import com.pos.inventoryfeature.dao.SupplierRepository;
import com.pos.inventoryfeature.dto.SupplierDto;
import com.pos.inventoryfeature.mapper.SupplierMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository repository;

    public void create(String name, String phone ,String email){
        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setPhoneNumber(phone);
        supplier.setEmail(email);
        repository.save(supplier);
    }

    public List<SupplierDto> getSuppliers(){
        return repository
                .findAll()
                .stream()
                .map(SupplierMapper::toDto).toList();
    }

    public Page<Supplier> findAll(String filter, PageRequest pageable) {
        return repository.findAll(filter, pageable);
    }

    public Optional<Supplier> findById(String id) {
        return repository.findById(UUID.fromString(id));
    }
}
