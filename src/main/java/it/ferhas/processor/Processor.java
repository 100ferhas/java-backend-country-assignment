package it.ferhas.processor;

import it.ferhas.rest_client.model.RestCountryModel;

import java.util.List;
import java.util.function.Consumer;

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
     * Consume data using a Consumer, to be used by forwarders that needs a text format
     */
    void consumeNormalizedData(Consumer<String> consumer);
}
