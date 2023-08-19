package it.ferhas.forwarder.impl;

import it.ferhas.forwarder.Forwarder;
import it.ferhas.processor.Processor;
import lombok.extern.slf4j.Slf4j;

/**
 * Forwarder class that prints to File
 */
@Slf4j(topic = "data_writer")
public class FileForwarder implements Forwarder {
    @Override
    public void forward(Processor processor) {
        String content = processor.getNormalizedData();
        log.info("### {}", processor.getDescription());
        log.info(content);
    }
}
