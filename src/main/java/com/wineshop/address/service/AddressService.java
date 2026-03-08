package com.wineshop.address.service;

import com.wineshop.address.dto.AddressCreateRequest;
import com.wineshop.address.dto.AddressUpdateRequest;
import com.wineshop.address.entity.WsAddress;

import java.util.List;

public interface AddressService {
    List<WsAddress> listMine();

    void add(AddressCreateRequest request);

    void update(Long id, AddressUpdateRequest request);

    void delete(Long id);

    void setDefault(Long id);
}
