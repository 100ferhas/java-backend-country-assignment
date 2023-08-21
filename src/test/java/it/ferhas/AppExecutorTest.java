package it.ferhas;


import feign.FeignException;
import it.ferhas.forwarder.ForwarderType;
import it.ferhas.rest_client.RestCountryClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppExecutorTest {

    @Mock
    private RestCountryClient countryApiClient;

    @Spy
    @InjectMocks
    private AppExecutor executor;

    @Test
    @DisplayName("Test execution passing null argument")
    public void testNullArgument() {
        when(countryApiClient.getCountries(anyList()))
                .thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> executor.execute(null));
    }

    @Test
    @DisplayName("Test execution passing file forwarder argument")
    public void testFileArgument() {
        when(countryApiClient.getCountries(anyList()))
                .thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> executor.execute(ForwarderType.FILE));
    }

    @Test
    @DisplayName("Test execution Failing request")
    public void testFailingRequest() {
        when(countryApiClient.getCountries(anyList()))
                .thenThrow(FeignException.FeignClientException.class);

        assertThrows(AppException.class, () -> executor.execute(ForwarderType.CONSOLE));
    }

    @Test
    @DisplayName("Test execution passing null to processors")
    public void testPassingNullToProcessors() {
        when(countryApiClient.getCountries(anyList()))
                .thenReturn(null);

        assertDoesNotThrow(() -> executor.execute(ForwarderType.CONSOLE));
    }
}
