package com.unexus.minegocio.service;

import com.unexus.minegocio.entity.Address;
import com.unexus.minegocio.entity.Client;
import com.unexus.minegocio.entity.enums.AddressType;
import com.unexus.minegocio.entity.enums.IdentificationType;

public class MiNegocioServiceTestHelper {

    public static Client buildClient() {
        return Client.builder()
                .id(0L)
                .identificationType(IdentificationType.CEDULA)
                .identificationNumber("123")
                .name("Name 123")
                .email("a@b.c")
                .cellphone("123")
                .build();
    }

    public static Address buildAddress() {
        return Address.builder()
                .id(0L)
                .province("A")
                .city("B")
                .address("C")
                .addressType(AddressType.MAIN)
                .client(buildClient())
                .build();
    }
}