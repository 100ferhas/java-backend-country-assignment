package it.ferhas.rest_client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class RestCountryModel {
    private RestCountryNameModel name;
    private String region;
    private List<String> borders;
    private Double area;
    private Long population;
    private String cca3;

    // add missing fields if needed
}
