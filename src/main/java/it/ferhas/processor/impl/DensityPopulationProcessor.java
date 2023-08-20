package it.ferhas.processor.impl;

import it.ferhas.processor.AbstractProcessor;
import it.ferhas.rest_client.model.RestCountryModel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class DensityPopulationProcessor extends AbstractProcessor {
    @Override
    public String getDescription() {
        return "Sorted list of countries by population density in descending order";
    }

    @Override
    public List<RestCountryModel> doProcess(List<RestCountryModel> countries) {
        // sort the collection based on population density
        Comparator<RestCountryModel> densityComparator = Comparator.comparingDouble(RestCountryModel::getDensityPopulation);
        countries.sort(Collections.reverseOrder(densityComparator));
        return new ArrayList<>(countries);
    }

    @Override
    public void consumeNormalizedData(Consumer<String> consumer) {
        List<RestCountryModel> data = getData();

        consumer.accept("------------------------------------------------------------------------------------");
        consumer.accept(String.format("| %-80s |", getDescription()));
        consumer.accept("------------------------------------------------------------------------------------");

        if (data == null || data.isEmpty()) {
            consumer.accept(String.format("| %-80s |", "No countries have been found."));
        } else {
            consumer.accept(String.format("| %-55s | %-22s |", "Country", "Population Density"));
            consumer.accept("------------------------------------------------------------------------------------");

            data.forEach(countryModel -> {
                if (countryModel.getName() != null) {
                    consumer.accept(String.format("| %-55s | %22.0f |", countryModel.getName().getCommon(), countryModel.getDensityPopulation()));

                } else if (countryModel.getCca3() != null) {
                    log.warn("Detected country without name, fallback to country code ({})!", countryModel.getCca3());
                    consumer.accept(String.format("| %-55s | %22.0f |", countryModel.getCca3(), countryModel.getDensityPopulation()));

                } else {
                    log.error("Detected country with neither name or country code...skipping it.");
                }
            });
        }

        consumer.accept("------------------------------------------------------------------------------------\n\n");
    }
}