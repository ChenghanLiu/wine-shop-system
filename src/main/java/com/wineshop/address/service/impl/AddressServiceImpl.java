package com.wineshop.address.service.impl;

import com.wineshop.address.dto.AddressCreateRequest;
import com.wineshop.address.dto.AddressUpdateRequest;
import com.wineshop.address.entity.WsAddress;
import com.wineshop.address.mapper.WsAddressMapper;
import com.wineshop.address.service.AddressService;
import com.wineshop.common.security.AuthHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final WsAddressMapper wsAddressMapper;

    public AddressServiceImpl(WsAddressMapper wsAddressMapper) {
        this.wsAddressMapper = wsAddressMapper;
    }

    @Override
    public List<WsAddress> listMine() {
        Long userId = AuthHelper.currentUser().getId();
        return wsAddressMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public void add(AddressCreateRequest request) {
        Long userId = AuthHelper.currentUser().getId();
        WsAddress address = new WsAddress();
        address.setUserId(userId);
        address.setReceiverName(request.getReceiverName());
        address.setReceiverPhone(request.getReceiverPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetailAddress(request.getDetailAddress());
        address.setIsDefault(request.getIsDefault() == null ? 0 : request.getIsDefault());

        if (address.getIsDefault() == 1) {
            wsAddressMapper.clearDefaultByUserId(userId);
        }
        wsAddressMapper.insert(address);
    }

    @Override
    @Transactional
    public void update(Long id, AddressUpdateRequest request) {
        Long userId = AuthHelper.currentUser().getId();
        WsAddress existed = wsAddressMapper.selectByIdAndUserId(id, userId);
        if (existed == null) {
            return;
        }
        if (request.getReceiverName() != null) existed.setReceiverName(request.getReceiverName());
        if (request.getReceiverPhone() != null) existed.setReceiverPhone(request.getReceiverPhone());
        if (request.getProvince() != null) existed.setProvince(request.getProvince());
        if (request.getCity() != null) existed.setCity(request.getCity());
        if (request.getDistrict() != null) existed.setDistrict(request.getDistrict());
        if (request.getDetailAddress() != null) existed.setDetailAddress(request.getDetailAddress());
        if (request.getIsDefault() != null) existed.setIsDefault(request.getIsDefault());

        if (existed.getIsDefault() == 1) {
            wsAddressMapper.clearDefaultByUserId(userId);
        }
        wsAddressMapper.updateByIdAndUserId(existed);
    }

    @Override
    public void delete(Long id) {
        Long userId = AuthHelper.currentUser().getId();
        wsAddressMapper.deleteByIdAndUserId(id, userId);
    }

    @Override
    @Transactional
    public void setDefault(Long id) {
        Long userId = AuthHelper.currentUser().getId();
        wsAddressMapper.clearDefaultByUserId(userId);
        wsAddressMapper.setDefaultByIdAndUserId(id, userId);
    }
}
