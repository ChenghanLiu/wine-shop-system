package com.wineshop.address.dto;

import lombok.Data;

@Data
public class AddressUpdateRequest {
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault;
}
