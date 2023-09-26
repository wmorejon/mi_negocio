package com.unexus.minegocio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

import com.unexus.minegocio.entity.Address;
import com.unexus.minegocio.entity.Client;
import com.unexus.minegocio.entity.enums.IdentificationType;
import com.unexus.minegocio.entity.dto.ClientDto;
import com.unexus.minegocio.entity.enums.ParameterType;
import com.unexus.minegocio.repository.AddressRepository;
import com.unexus.minegocio.repository.ClientRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MiNegocioServiceTest {

    @Mock
    ClientRepository clientRepository;

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    MiNegocioService miNegocioService;

    @Test
    void shouldReturnClientDtos_whenParameterTypeIsByIdentification() {
        Client client = MiNegocioServiceTestHelper.buildClient();
        List<Client> clientsResponse = Collections.singletonList(client);
        List<Address> addresses = Collections.singletonList(MiNegocioServiceTestHelper.buildAddress());

        Mockito.when(clientRepository.findByIdentificationNumberContaining("123"))
                .thenReturn(clientsResponse);
        Mockito.when(addressRepository.findByClientId(0L))
                .thenReturn(addresses);

        List<ClientDto> clients = miNegocioService.getClientsByIdentificationOrName(ParameterType.BY_IDENTIFICATION, "123");

        assertEquals(1, clients.size());
        assertEquals(IdentificationType.CEDULA, clients.get(0).getIdentificationType());
        assertEquals("123", clients.get(0).getIdentificationNumber());
        // TODO: add missing assertions
    }

    @Test
    void shouldReturnClientDtos_whenParameterTypeIsByName() {
        Client client = MiNegocioServiceTestHelper.buildClient();
        client.setIdentificationType(IdentificationType.RUC);
        List<Client> clientsResponse = Collections.singletonList(client);

        Mockito.when(clientRepository.findByNameContaining("Name"))
                .thenReturn(clientsResponse);

        List<ClientDto> clients = miNegocioService.getClientsByIdentificationOrName(ParameterType.BY_NAME, "Name");

        assertEquals(1, clients.size());
    }

    @Test
    void shouldThrowRuntimeException() {
        ClientDto clientDto = new ClientDto();
        clientDto.setIdentificationType(IdentificationType.CEDULA);
        clientDto.setIdentificationNumber("1");
        clientDto.setName("Name 1");
        clientDto.setEmail("a@x.y");
        clientDto.setCellphone("123");
        clientDto.setMainProvince("A");
        clientDto.setMainCity("B");
        clientDto.setMainAddress("C");

        Mockito.when(clientRepository.findByIdentificationNumber(anyString()))
                .thenReturn(Optional.of(MiNegocioServiceTestHelper.buildClient()));

        RuntimeException runtimeException = assertThrows(
                RuntimeException.class,
                () -> miNegocioService.createClient(clientDto)
        );

        assertNotNull(runtimeException);
        assertEquals("Client id already exists", runtimeException.getMessage());
    }

    @Test
    void updateClient() {
    }

    @Test
    void deleteClient() {
    }

    @Test
    void addAddressToClient() {
    }

    @Test
    void listAddresses() {
    }
}