package com.wineshop.address.controller;

import com.wineshop.address.dto.AddressCreateRequest;
import com.wineshop.address.dto.AddressUpdateRequest;
import com.wineshop.address.entity.WsAddress;
import com.wineshop.address.service.AddressService;
import com.wineshop.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public Result<List<WsAddress>> list() {
        return Result.ok(addressService.listMine());
    }

    @PostMapping
    public Result<Void> add(@Valid @RequestBody AddressCreateRequest request) {
        addressService.add(request);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody AddressUpdateRequest request) {
        addressService.update(id, request);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return Result.ok();
    }

    @PutMapping("/{id}/default")
    public Result<Void> setDefault(@PathVariable Long id) {
        addressService.setDefault(id);
        return Result.ok();
    }
}
