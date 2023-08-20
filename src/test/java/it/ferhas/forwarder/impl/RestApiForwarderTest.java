package it.ferhas.forwarder.impl;

import it.ferhas.processor.impl.DensityPopulationProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RestApiForwarderTest {

    @Spy
    private RestApiForwarder forwarder;

    @Mock
    private DensityPopulationProcessor processor;

    @Test
    @DisplayName("Test forwarding throws")
    public void testToBeImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> forwarder.forward(processor));
    }
}
