package com.unexus.minegocio.service;

import com.unexus.minegocio.entity.Address;
import com.unexus.minegocio.entity.enums.AddressType;
import com.unexus.minegocio.entity.Client;
import com.unexus.minegocio.entity.dto.ClientDto;
import com.unexus.minegocio.entity.dto.AddressDto;
import com.unexus.minegocio.entity.enums.ParameterType;
import com.unexus.minegocio.repository.AddressRepository;
import com.unexus.minegocio.repository.ClientRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiNegocioService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AddressRepository addressRepository;

    public List<ClientDto> getClientsByIdentificationOrName(
            ParameterType parameterType, String param) {

        if (parameterType.equals(ParameterType.BY_IDENTIFICATION)) {
            return mapClientToClientDto(clientRepository.findByIdentificationNumberContaining(param));
        }

        return mapClientToClientDto(clientRepository.findByNameContaining(param));
    }

    public ClientDto createClient(ClientDto clientDto) throws RuntimeException {

        if (clientRepository
                .findByIdentificationNumber(clientDto.getIdentificationNumber())
                .isPresent()) {

            throw new RuntimeException("Client id already exists");
        }

        Client client = Client.builder()
                .identificationType(clientDto.getIdentificationType())
                .identificationNumber(clientDto.getIdentificationNumber())
                .name(clientDto.getName())
                .email(clientDto.getEmail())
                .cellphone(clientDto.getCellphone())
                .build();

        Address address = Address.builder()
                .province(clientDto.getMainProvince())
                .city(clientDto.getMainCity())
                .address(clientDto.getMainAddress())
                .addressType(AddressType.MAIN)
                .client(client)
                .build();

        clientRepository.save(client);
        addressRepository.save(address);

        return clientDto;
    }

    public ClientDto updateClient(String identificationNumber, ClientDto clientDto) {
        Optional<Client> optClient = clientRepository.findByIdentificationNumber(identificationNumber);

        if (optClient.isEmpty()) {
            throw new RuntimeException("Client not found");
        }

        Client client = optClient.get();
        Optional<Client> byIdentificationNumber =
                clientRepository.findByIdentificationNumber(clientDto.getIdentificationNumber());
        if (byIdentificationNumber.isPresent()
                && !client.getIdentificationNumber().equals(clientDto.getIdentificationNumber())) {

            throw new RuntimeException("Client id already exists");
        }

        ClientDto response = new ClientDto();

        if (clientDto.getIdentificationType() != null) {
            client.setIdentificationType(clientDto.getIdentificationType());
            response.setIdentificationType(clientDto.getIdentificationType());
        } else {
            response.setIdentificationType(client.getIdentificationType());
        }

        if (clientDto.getIdentificationNumber() != null
                && !clientDto.getIdentificationNumber().isBlank()) {
            client.setIdentificationNumber(clientDto.getIdentificationNumber());
            response.setIdentificationNumber(clientDto.getIdentificationNumber());
        }

        if (clientDto.getName() != null && !clientDto.getName().isBlank()) {
            client.setName(clientDto.getName());
            response.setName(clientDto.getName());
        }

        if (clientDto.getEmail() != null && clientDto.getEmail().isBlank()) {
            client.setEmail(clientDto.getEmail());
            response.setEmail(clientDto.getEmail());
        }

        if (clientDto.getCellphone() != null && clientDto.getCellphone().isBlank()) {
            client.setCellphone(clientDto.getCellphone());
            response.setCellphone(clientDto.getCellphone());
        }

        clientRepository.save(client);

        return response;
    }

    public void deleteClient(String identificationNumber) {
        Optional<Client> optClient = clientRepository.findByIdentificationNumber(identificationNumber);

        if (optClient.isEmpty()) {
            throw new RuntimeException("Client not found");
        }

        Client client = optClient.get();
        addressRepository.findByClientId(client.getId())
                .forEach(address -> addressRepository.delete(address));

        clientRepository.delete(client);
    }

    public void addAddressToClient(String identificationNumber, AddressDto addressDto) {
        Optional<Client> optClient = clientRepository.findByIdentificationNumber(identificationNumber);

        if (optClient.isEmpty()) {
            throw new RuntimeException("Client not found");
        }

        Client client = optClient.get();
        Address address = Address.builder()
                .province(addressDto.getProvince())
                .city(addressDto.getCity())
                .address(addressDto.getAddress())
                .client(client)
                .addressType(AddressType.SECONDARY)
                .build();

        addressRepository.save(address);
    }

    public List<AddressDto> listAddresses(String identificationNumber) {
        Optional<Client> optClient = clientRepository.findByIdentificationNumber(identificationNumber);

        if (optClient.isEmpty()) {
            throw new RuntimeException("Client not found");
        }

        Client client = optClient.get();

        return addressRepository.findByClientId(client.getId())
                .stream()
                .map(address -> AddressDto.builder()
                        .province(address.getProvince())
                        .city(address.getCity())
                        .address(address.getAddress())
                        .build()
                )
                .toList();
    }

    private List<ClientDto> mapClientToClientDto(List<Client> clients) {
        return clients
                .stream()
                .map(client -> {
                    Optional<Address> optMainAddress = addressRepository
                            .findByClientId(client.getId())
                            .stream()
                            .filter(address -> address.getAddressType().equals(AddressType.MAIN))
                            .findFirst();

                    Address mainAddress = new Address();
                    if (optMainAddress.isPresent()) {
                        mainAddress = optMainAddress.get();
                    }

                    return ClientDto.builder()
                            .identificationType(client.getIdentificationType())
                            .identificationNumber(client.getIdentificationNumber())
                            .name(client.getName())
                            .email(client.getEmail())
                            .cellphone(client.getCellphone())
                            .mainProvince(mainAddress.getProvince())
                            .mainCity(mainAddress.getCity())
                            .mainAddress(mainAddress.getAddress())
                            .build();
                })
                .toList();
    }

}
