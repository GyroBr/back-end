package com.Gyro.back_end_gyro.domain.address.dto;

import com.Gyro.back_end_gyro.domain.address.entity.Address;

public record AddressResponseDTO(
        String postalCode,


        String street,


        String neighborhood,


        String federativeUnity,


        String city,



        String number
) {

    public AddressResponseDTO(Address address) {
        this(address.getPostalCode(), address.getStreet(), address.getNeighborhood(), address.getFederativeUnity(), address.getCity(), address.getNumber());
    }
}
