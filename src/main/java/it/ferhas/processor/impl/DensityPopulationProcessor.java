package it.ferhas.processor.impl;

import it.ferhas.processor.AbstractProcessor;
import it.ferhas.rest_client.model.RestCountryModel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class DensityPopulationProcessor extends AbstractProcessor {
    @Override
    public String getDescription() {
        return "Sorted list of countries by population density in descending order:";
    }

    @Override
    public List<RestCountryModel> doProcess(List<RestCountryModel> countries) {
        if (countries == null) {
            return null;
        } else {
            // sort the collection based on population density
            Comparator<RestCountryModel> densityComparator = Comparator.comparingDouble(RestCountryModel::getDensityPopulation);
            countries.sort(Collections.reverseOrder(densityComparator));
            return new ArrayList<>(countries);
        }
    }

    @Override
    public String getNormalizedData() {
        StringBuilder result = new StringBuilder();
        List<RestCountryModel> data = getData();

        if (data == null || data.isEmpty()) {
            result.append("No countries have been found.");
        } else {
            result.append("-----------------------------------------------------------------------------\n");
            result.append(String.format("| %-50s | %-20s |%n", "Country", "Population Density"));
            result.append("-----------------------------------------------------------------------------\n");

            data.forEach(countryModel -> {
                if (countryModel.getName() != null) {
                    result.append(String.format("| %-50s | %20.0f |%n", countryModel.getName().getCommon(), countryModel.getDensityPopulation()));

                } else if (countryModel.getCca3() != null) {
                    log.warn("Detected country without name, fallback to country code ({})!", countryModel.getCca3());
                    result.append(String.format("| %-50s | %20.0f |%n", countryModel.getCca3(), countryModel.getDensityPopulation()));

                } else {
                    log.error("Detected country with neither name or country code...skipping it.");
                }
            });

            result.append("-----------------------------------------------------------------------------");
        }

        return result.toString();
    }
}