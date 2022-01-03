package com.github.elenaAeternaNox.rest_api.models.reqres.single_resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResourceData {
    private String color;
    private Long id;
    private String name;
    @JsonProperty("pantone_value")
    private String pantoneValue;
    private Long year;

}
