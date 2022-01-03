package com.github.elenaAeternaNox.rest_api.models.reqres;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Registr {
    private Integer id;
    private String token;
    private String error;
}
