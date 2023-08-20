package it.ferhas.processor.impl;

import it.ferhas.processor.AbstractProcessor;
import it.ferhas.rest_client.model.RestCountryModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class AsianBorderProcessor extends AbstractProcessor {
    private final String ASIA = "Asia";

    @Override
    public String getDescription() {
        return "Countries in Asia containing the most bordering countries of a different region:";
    }

    @Override
    protected List<RestCountryModel> doProcess(List<RestCountryModel> countries) {
        // filter those invalid data having null values in countryCode or region fields
        // that are required to find our results
        countries = countries.stream()
                .filter(countryModel -> countryModel.getCca3() != null && countryModel.getRegion() != null)
                .collect(Collectors.toList());

        // create a map with key "countryCode" and value "region" for all countries
        // we will use this map to count non-Asian countries for each Asian country
        Map<String, String> countryRegionMapping = countries.stream()
                .collect(Collectors.toMap(RestCountryModel::getCca3, RestCountryModel::getRegion));

        // filter Asian countries first
        // then find those (we assume can be multiple) with most non-Asian borders countries
        return countries.stream()
                .filter(countryModel -> countryModel.getRegion().strip().equalsIgnoreCase(ASIA))
                .collect(Collectors.groupingBy(country ->
                        country.getBorders()
                                .stream()
                                .filter(countryCode -> !countryRegionMapping.getOrDefault(countryCode, "").strip().equalsIgnoreCase(ASIA))
                                .count()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    @Override
    public String getNormalizedData() {
        StringBuilder result = new StringBuilder();
        List<RestCountryModel> data = getData();

        if (data == null || data.isEmpty()) {
            result.append("No countries have been found.");
        } else {
            data.forEach(countryModel -> {
                if (countryModel.getName() != null) {
                    result.append(countryModel.getName().getCommon());
                    result.append(System.lineSeparator());

                } else if (countryModel.getCca3() != null) {
                    log.warn("Detected country without name, fallback to country code ({})!", countryModel.getCca3());
                    result.append(countryModel.getCca3());
                    result.append(System.lineSeparator());

                } else {
                    log.error("Detected country with neither name or country code...skipping it.");
                }
            });
        }

        return result.toString();
    }
}
