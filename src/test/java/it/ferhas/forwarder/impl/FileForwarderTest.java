package it.ferhas.forwarder.impl;

import it.ferhas.processor.impl.DensityPopulationProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("File Forwarder Tests")
public class FileForwarderTest {

    @Spy
    private FileForwarder forwarder;

    @Mock
    private DensityPopulationProcessor processor;

    @Test
    @DisplayName("Test forwarding to file doesn't throws errors with null values")
    public void testNull() {
        when(processor.getNormalizedData()).thenReturn(null);
        assertDoesNotThrow(() -> forwarder.forward(processor));
    }


    @Test
    @DisplayName("Test forwarding to file doesn't throws errors with values")
    public void testValue() {
        when(processor.getNormalizedData()).thenReturn("a value");
        assertDoesNotThrow(() -> forwarder.forward(processor));
    }
}
