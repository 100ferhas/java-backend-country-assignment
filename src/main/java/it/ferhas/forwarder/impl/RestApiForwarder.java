package it.ferhas.forwarder.impl;

import it.ferhas.forwarder.Forwarder;
import it.ferhas.processor.Processor;
import it.ferhas.rest_client.model.RestCountryModel;

import java.util.List;

/**
 * Forwarder class that forward processed data to a REST API
 */
public class RestApiForwarder implements Forwarder {
    @Override
    public void forward(Processor processor) {
        List<RestCountryModel> data = processor.getData();

        // todo to be implemented
        throw new UnsupportedOperationException("RestApiForwarder is not implemented yet! please avoid using it.");
    }
}
