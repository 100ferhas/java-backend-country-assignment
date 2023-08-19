package it.ferhas.forwarder.impl;

import it.ferhas.forwarder.Forwarder;
import it.ferhas.processor.Processor;

/**
 * Forwarder class that prints to console
 */
public class ConsoleForwarder implements Forwarder {
    @Override
    public void forward(Processor processor) {
        String content = processor.getNormalizedData();

        if (content != null) {
            System.out.println(processor.getDescription());
            System.out.println(content);
            System.out.println();
        }
    }
}
