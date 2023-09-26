package com.unexus.minegocio.controller.impl;

import com.unexus.minegocio.controller.IMiNegocioController;
import com.unexus.minegocio.entity.dto.AddressDto;
import com.unexus.minegocio.entity.dto.ClientDto;
import com.unexus.minegocio.service.MiNegocioService;
import com.unexus.minegocio.entity.enums.ParameterType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MiNegocioControllerImpl implements IMiNegocioController {

    @Autowired
    MiNegocioService miNegocioService;

    @Override
    public ResponseEntity<List<ClientDto>> getClients(ParameterType parameterType, String param) {
        return ResponseEntity.ok(
                miNegocioService.getClientsByIdentificationOrName(parameterType, param)
        );
    }

    @Override
    public ResponseEntity<ClientDto> createClient(ClientDto clientDto) {
        try {
            return ResponseEntity.ok(miNegocioService.createClient(clientDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    public ResponseEntity<ClientDto> updateClient(String identificationNumber, ClientDto clientDto) {
        return ResponseEntity.ok(miNegocioService.updateClient(identificationNumber, clientDto));
    }

    @Override
    public ResponseEntity<String> deleteClient(String identificationNumber) {
        miNegocioService.deleteClient(identificationNumber);

        return ResponseEntity.ok("ok");
    }

    @Override
    public  ResponseEntity<String> addAddressClient(
            String identificationNumber,
            AddressDto addressDto
    ) {

        miNegocioService.addAddressToClient(identificationNumber, addressDto);
        return ResponseEntity.ok("ok");
    }

    @Override
    public ResponseEntity<List<AddressDto>> getAdditionalAddressesClient(String identificationNumber) {
        return ResponseEntity.ok(miNegocioService.listAddresses(identificationNumber));
    }
}
