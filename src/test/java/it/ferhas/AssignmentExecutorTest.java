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
@DisplayName("Assignment Executor Tests")
public class AssignmentExecutorTest {

    @Mock
    private RestCountryClient countryApiClient;

    @Spy
    @InjectMocks
    private AssignmentExecutor executor;

    @Test
    @DisplayName("Test passing null argument")
    public void testNullArgument() {
        when(countryApiClient.getCountries(anyList())).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> executor.execute(null));
    }

    @Test
    @DisplayName("Test passing file forwarder argument")
    public void testFileArgument() {
        when(countryApiClient.getCountries(anyList())).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> executor.execute(ForwarderType.FILE));
    }

    @Test
    @DisplayName("Test passing kafka forwarder argument")
    public void testKafkaArgument() {
        when(countryApiClient.getCountries(anyList())).thenReturn(new ArrayList<>());
        assertThrows(UnsupportedOperationException.class, () -> executor.execute(ForwarderType.KAFKA));
    }

    @Test
    @DisplayName("Test passing rest forwarder argument")
    public void testRestArgument() {
        when(countryApiClient.getCountries(anyList())).thenReturn(new ArrayList<>());
        assertThrows(UnsupportedOperationException.class, () -> executor.execute(ForwarderType.REST));
    }

    @Test
    @DisplayName("Test Failing request")
    public void testFailingRequest() {
        when(countryApiClient.getCountries(anyList())).thenThrow(FeignException.FeignClientException.class);

        assertThrows(AppException.class, () -> executor.execute(null));
    }
}
