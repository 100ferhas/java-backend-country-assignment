package it.ferhas.forwarder.impl;

import it.ferhas.forwarder.Forwarder;
import it.ferhas.processor.Processor;

/**
 * Forwarder class that prints to console
 */
public class ConsoleForwarder implements Forwarder {
    @Override
    public void forward(Processor processor) {
        processor.consumeNormalizedData(text -> {
            if (text != null) {
                System.out.println(text);
            }
        });
    }
}
