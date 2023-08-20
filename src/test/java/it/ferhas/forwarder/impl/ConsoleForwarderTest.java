package it.ferhas.forwarder.impl;

import it.ferhas.processor.impl.DensityPopulationProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsoleForwarderTest {

    @Spy
    private ConsoleForwarder forwarder;

    @Mock
    private DensityPopulationProcessor processor;

    @Test
    @DisplayName("Test forwarding to console doesn't throws errors with null values")
    public void testNull() {
        when(processor.getNormalizedData()).thenReturn(null);
        assertDoesNotThrow(() -> forwarder.forward(processor));
    }


    @Test
    @DisplayName("Test forwarding to console doesn't throws errors with values")
    public void testValue() {
        when(processor.getNormalizedData()).thenReturn("a value");
        assertDoesNotThrow(() -> forwarder.forward(processor));
    }
}
