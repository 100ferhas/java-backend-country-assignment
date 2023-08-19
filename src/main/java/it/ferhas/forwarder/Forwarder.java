package it.ferhas.forwarder;

import it.ferhas.processor.Processor;

/**
 * Forwarder interface, defining contract for implementations
 */
public interface Forwarder {
    /**
     * @param processor data processor used to get internal data with needed format
     */
    void forward(Processor processor);
}
