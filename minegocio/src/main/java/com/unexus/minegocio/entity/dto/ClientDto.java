package com.unexus.minegocio.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.unexus.minegocio.entity.IdentificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private IdentificationType identificationType;
    private String identificationNumber;
    private String name;
    private String email;
    private String cellphone;
    private String mainProvince;
    private String mainCity;
    private String mainAddress;

}
