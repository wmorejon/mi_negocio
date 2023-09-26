package com.unexus.minegocio.controller;

import com.unexus.minegocio.entity.Client;
import com.unexus.minegocio.entity.dto.AddressDto;
import com.unexus.minegocio.entity.dto.ClientDto;
import com.unexus.minegocio.service.ParameterType;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unexus/v1/minegocio")
public interface IMiNegocioController {

    @GetMapping("/clients")
    ResponseEntity<List<ClientDto>> getClients(
            @RequestParam ParameterType parameterType,
            @RequestParam String param
    );

    @PostMapping("/client")
    ResponseEntity<ClientDto> createClient(@RequestBody ClientDto client);

    @PutMapping("/client/{identificationNumber}")
    ResponseEntity<ClientDto> updateClient(
            @PathVariable String identificationNumber,
            @RequestBody ClientDto clientDto
    );

    @DeleteMapping("/client/{identificationNumber}")
    ResponseEntity<String> deleteClient(@PathVariable String identificationNumber);

    @PostMapping("/address/{identificationNumber}")
    ResponseEntity<String> addAddressClient(
            @PathVariable String identificationNumber,
            @RequestBody AddressDto addressDto
    );

    @GetMapping("/addresses/{identificationNumber}")
    ResponseEntity<List<AddressDto>> getAdditionalAddressesClient(@PathVariable String identificationNumber);

}
