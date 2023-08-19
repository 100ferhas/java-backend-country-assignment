package it.ferhas.processor.impl;

import it.ferhas.rest_client.model.RestCountryModel;
import it.ferhas.rest_client.model.RestCountryNameModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test finding a sorted list of countries by population density in descending order")
public class DensityPopulationProcessorTest {
    @Spy
    @InjectMocks
    private DensityPopulationProcessor processor;

    @Spy
    private List<RestCountryModel> data = new ArrayList<>();

    @Test
    @DisplayName("Given null value, don't throw exceptions")
    public void testNullArgument() {
        processor.process(null);
        List<RestCountryModel> sortedCountries = processor.getData();
        assertNull(sortedCountries);
    }

    @Test
    @DisplayName("Given valid list of countries, sort correctly")
    public void testDensityPopulation() {
        List<RestCountryModel> countries = new ArrayList<>() {{
            // boundary case
            add(RestCountryModel.builder()
                    .name(RestCountryNameModel.builder().common("Negative area value").build())
                    .area(-1D)
                    .population(10L)
                    .build());

            add(RestCountryModel.builder()
                    .name(RestCountryNameModel.builder().common("Biggest density").build())
                    .area(1D)
                    .population(1000L)
                    .build());

            add(RestCountryModel.builder()
                    .name(RestCountryNameModel.builder().common("Medium density 1").build())
                    .area(250D)
                    .population(1L)
                    .build());

            add(RestCountryModel.builder()
                    .name(RestCountryNameModel.builder().common("Medium density 2").build())
                    .area(500D)
                    .population(2L)
                    .build());

            add(RestCountryModel.builder()
                    .name(RestCountryNameModel.builder().common("Smallest density").build())
                    .area(1000D)
                    .population(1L)
                    .build());

            // boundary case
            add(RestCountryModel.builder()
                    .name(RestCountryNameModel.builder().common("Zero values").build())
                    .area(0D)
                    .population(0L)
                    .build());

            // boundary case
            add(RestCountryModel.builder()
                    .name(RestCountryNameModel.builder().common("null values").build())
                    .area(null)
                    .population(null)
                    .build());
        }};

        processor.process(countries);
        List<RestCountryModel> sortedCountries = processor.getData();

        assertNotNull(sortedCountries, "Sorted countries should not be null");
        assertEquals(7, sortedCountries.size(), "Unexpected country size");
        assertEquals("Biggest density", sortedCountries.get(0).getName().getCommon(), "Unexpected item at position 0");
        assertEquals("Medium density 1", sortedCountries.get(1).getName().getCommon(), "Unexpected item at position 1");
        assertEquals("Medium density 2", sortedCountries.get(2).getName().getCommon(), "Unexpected item at position 2");
        assertEquals("Smallest density", sortedCountries.get(3).getName().getCommon(), "Unexpected item at position 3");
        assertEquals("Negative area value", sortedCountries.get(4).getName().getCommon(), "Unexpected item at position 4");
        assertEquals("Zero values", sortedCountries.get(5).getName().getCommon(), "Unexpected item at position 5");
        assertEquals("null values", sortedCountries.get(6).getName().getCommon(), "Unexpected item at position 5");
    }

    @Test
    @DisplayName("Get normalized data to be print having null value")
    public void getNormalizedDataNullValue() {
        data = null;
        String normalizedData = processor.getNormalizedData();

        assertNotNull(normalizedData, "Expected some data");
        assertEquals("No countries have been found.", normalizedData, "Unexpected data");
    }

    @Test
    @DisplayName("Get normalized data to be print")
    public void getNormalizedData() {
        // this country has the name
        data.add(RestCountryModel.builder().name(RestCountryNameModel.builder().common("Italy").build()).build());
        // this country doesn't have the name, so the country code will be shown
        data.add(RestCountryModel.builder().cca3("ITA").build());
        // this country should be shown, but it doesn't have netiher name or country code so will be skipped
        data.add(RestCountryModel.builder().build());

        String normalizedData = processor.getNormalizedData();

        StringBuilder expected = new StringBuilder();

        expected.append("-----------------------------------------------------------------------------\n");
        expected.append("| Country                                            | Population Density   |\n");
        expected.append("-----------------------------------------------------------------------------\n");
        expected.append("| Italy                                              |                 0,00 |\n");
        expected.append("| ITA                                                |                 0,00 |\n");
        expected.append("-----------------------------------------------------------------------------\n");

        assertNotNull(normalizedData, "Expected some data");
        assertEquals(expected.toString(), normalizedData, "Unexpected data");
    }
}
