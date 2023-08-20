package it.ferhas.forwarder.impl;

import it.ferhas.processor.impl.DensityPopulationProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class FileForwarderTest {
    @Spy
    private FileForwarder forwarder;

    @Mock
    private DensityPopulationProcessor processor;

    @Test
    @DisplayName("Test forwarding to file doesn't throws errors with null values")
    public void testNull() {
        doAnswer(invocationOnMock -> {
            Consumer<String> consumer = invocationOnMock.getArgument(0);
            consumer.accept(null);
            return null;
        }).when(processor).consumeNormalizedData(any());

        assertDoesNotThrow(() -> forwarder.forward(processor));
    }


    @Test
    @DisplayName("Test forwarding to file doesn't throws errors with values")
    public void testValue() {
        doAnswer(invocationOnMock -> {
            Consumer<String> consumer = invocationOnMock.getArgument(0);
            consumer.accept("value");
            return null;
        }).when(processor).consumeNormalizedData(any());

        assertDoesNotThrow(() -> forwarder.forward(processor));
    }
}
