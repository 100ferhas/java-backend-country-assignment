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

    // todo add missing fields if needed

    public Double getDensityPopulation() {
        // in some cases we have invalid values in response (for example area = -1)
        // in this case we consider as 0 density population
        if (area != null && area > 0) {
            return population / area;
        }

        return 0D;
    }
}
