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
public class AsianBorderProcessorTest {
    @Spy
    @InjectMocks
    private AsianBorderProcessor processor;

    @Spy
    private List<RestCountryModel> data = new ArrayList<>();

    @Test
    @DisplayName("Check passing null argument doesn't throws excepions but just returns null")
    public void testNullPass() {
        processor.process(null);
        List<RestCountryModel> result = processor.getData();
        assertNull(result);
    }

    @Test
    @DisplayName("Check if the correct country is found")
    public void testFindCorrectCountry() {
        // we'll build some fake data to check if algorithm is working
        // we'll build a matrix representing the world, having 2x2 matrix representing Asia
        // A are Asian countries and X and Y are not
        // [ X X X ]
        // [ X A A ]
        // [ Y A A ]
        // [       ]
        // [   Y   ]

        List<RestCountryModel> countries = new ArrayList<>() {{
            add(RestCountryModel.builder()
                    .borders(List.of("1x2", "2x1", "2x2"))
                    .cca3("1x1")
                    .region("Europe")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("1x1", "1x3", "2x1", "2x2", "2x3"))
                    .cca3("1x2")
                    .region("Europe")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("1x2", "2x2", "2x3"))
                    .cca3("1x3")
                    .region("Europe")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("1x1", "1x2", "2x2", "3x1", "3x2"))
                    .cca3("2x1")
                    .region("Europe")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("1x1", "1x2", "1x3", "2x1", "2x3", "3x1", "3x2", "3x3"))
                    .cca3("2x2")
                    .region("Asia")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("1x2", "1x3", "2x2", "3x2", "3x3"))
                    .cca3("2x3")
                    .region("asia") // boundary case
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("2x1", "2x2", "3x2"))
                    .cca3("3x1")
                    .region("Africa")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("2x1", "2x2", "2x3", "3x1", "3x3"))
                    .cca3("3x2")
                    .region(" Asia ") // boundary case
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("2x2", "2x3", "3x2"))
                    .cca3("3x3")
                    .region("Asia")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of()) // boundary case
                    .cca3("5x2")
                    .region("Africa")
                    .build());

            // we add an extreme boundary case with all null properties
            add(RestCountryModel.builder()
                    .borders(null) // boundary case
                    .cca3(null) // boundary case
                    .region(null) // boundary case
                    .build());
        }};

        processor.process(countries);
        List<RestCountryModel> result = processor.getData();

        assertNotNull(result, "At least 1 country was expected");
        assertFalse(result.isEmpty(), "At least 1 country was expected");
        assertEquals(1, result.size(), "Expected 1 results");
        assertEquals("2x2", result.get(0).getCca3(), "Was expected to return the 2x2 country");
    }

    @Test
    @DisplayName("Check if the correct country is found when multiple countries have the same borders number")
    public void testFindCorrectCountryDuplicates() {
        // we'll build some fake data to check if algorithm is working
        // we'll build a matrix representing the world, expecting as result 2 Asian countries with same number of
        // non-Asian border countries
        // [ X A ]
        // [ A Y ]

        List<RestCountryModel> countries = new ArrayList<>() {{
            add(RestCountryModel.builder()
                    .borders(List.of("1x2", "2x1", "2x2"))
                    .cca3("1x1")
                    .region("Europe")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("1x1", "1x3", "2x1", "2x2", "2x3")) // test non-existing borders
                    .cca3("1x2")
                    .region("Asia")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("1x1", "1x2", "2x2", "3x1", "3x2")) // test non-existing borders
                    .cca3("2x1")
                    .region("Asia")
                    .build());

            add(RestCountryModel.builder()
                    .borders(List.of("1x1", "1x2", "2x1"))
                    .cca3("2x2")
                    .region("Africa")
                    .build());
        }};

        processor.process(countries);
        List<RestCountryModel> result = processor.getData();

        assertNotNull(result, "At least 1 country was expected");
        assertFalse(result.isEmpty(), "At least 1 country was expected");
        assertEquals(2, result.size(), "Expected 2 results");
        assertEquals("1x2", result.get(0).getCca3(), "Was expected to return the 1x2 country");
        assertEquals("2x1", result.get(1).getCca3(), "Was expected to return the 2x1 country");
    }

    @Test
    @DisplayName("Get normalized data to be print having null value")
    public void getNormalizedDataNullValue() {
        data = null;

        StringBuilder result = new StringBuilder();
        processor.consumeNormalizedData(text -> {
            result.append(text);
            result.append(System.lineSeparator());
        });

        StringBuilder expected = new StringBuilder();
        expected.append("------------------------------------------------------------------------------------\n");
        expected.append("| Countries in Asia containing the most bordering countries of a different region  |\n");
        expected.append("------------------------------------------------------------------------------------\n");
        expected.append("| No countries have been found.                                                    |\n");
        expected.append("------------------------------------------------------------------------------------\n\n\n");


        assertNotNull(result.toString(), "Expected some data");
        assertEquals(expected.toString(), result.toString(), "Unexpected data");
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

        StringBuilder result = new StringBuilder();
        processor.consumeNormalizedData(text -> {
            result.append(text);
            result.append(System.lineSeparator());
        });

        StringBuilder expected = new StringBuilder();
        expected.append("------------------------------------------------------------------------------------\n");
        expected.append("| Countries in Asia containing the most bordering countries of a different region  |\n");
        expected.append("------------------------------------------------------------------------------------\n");
        expected.append("| Italy                                                                            |\n");
        expected.append("| ITA                                                                              |\n");
        expected.append("------------------------------------------------------------------------------------\n\n\n");

        assertNotNull(result.toString(), "Expected some data");
        assertEquals(expected.toString(), result.toString(), "Unexpected data");
    }
}