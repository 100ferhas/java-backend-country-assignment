package it.ferhas.processor;

import it.ferhas.rest_client.model.RestCountryModel;

import java.util.List;

/**
 * Processor interface, define methods to process data retrieved from Country Rest APIs
 */
public interface Processor {
    /**
     * @return Processor's description, used as title when results are forwarded as text
     */
    String getDescription();

    /**
     * Actual data processing
     *
     * @param countries Countries retrieved from the APIs
     */
    void process(List<RestCountryModel> countries);

    /**
     * @return internal stored data
     */
    List<RestCountryModel> getData();

    /**
     * Convert data to String to be used by forwarders that needs a text format
     *
     * @return String containing text of the resulting process
     */
    String getNormalizedData();
}
