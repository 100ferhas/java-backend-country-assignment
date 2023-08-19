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
        if (countries == null) {
            return null;
        } else {
            // filter those invalid data having null values in countryCode or region, that are needed to find our result
            countries = countries.stream()
                    .filter(countryModel -> countryModel.getCca3() != null && countryModel.getRegion() != null)
                    .collect(Collectors.toList());

            // create a map with key "countryCode" and value "region" for all countries
            Map<String, String> countryRegionMapping = countries.stream()
                    .collect(Collectors.toMap(RestCountryModel::getCca3, RestCountryModel::getRegion));

            // filter Asian countries and find those (we assume can be multiple) with most borders countries not in Asia
            return countries.stream()
                    .filter(countryModel -> countryModel.getRegion().strip().equalsIgnoreCase(ASIA))
                    .collect(Collectors.groupingBy(country ->
                            country.getBorders()
                                    .stream()
                                    .filter(s -> !countryRegionMapping.getOrDefault(s, "").strip().equalsIgnoreCase(ASIA)).count()
                    ))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByKey())
                    .map(Map.Entry::getValue)
                    .orElse(null);
        }
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
