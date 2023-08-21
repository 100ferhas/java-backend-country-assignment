package it.ferhas.rest_client;

import feign.Param;
import feign.RequestLine;
import it.ferhas.rest_client.model.RestCountryModel;

import java.util.List;

/**
 * Feign client class used to retrieve countries data from APIs
 */
public interface RestCountryClient {
    @RequestLine("GET /all")
    List<RestCountryModel> getCountries();

    @RequestLine("GET /all?fields={fields}")
    List<RestCountryModel> getCountries(@Param("fields") List<String> fields);
}
