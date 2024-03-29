package it.ferhas;

import feign.Feign;
import feign.FeignException;
import feign.jackson.JacksonDecoder;
import it.ferhas.forwarder.Forwarder;
import it.ferhas.forwarder.ForwarderProvider;
import it.ferhas.forwarder.ForwarderType;
import it.ferhas.processor.Processor;
import it.ferhas.processor.ProcessorProvider;
import it.ferhas.rest_client.RestCountryClient;
import it.ferhas.rest_client.RestErrorDecoder;
import it.ferhas.rest_client.model.RestCountryModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Executor that runs all the processes configured in the processors List
 */
@Slf4j
public class AppExecutor {
    @SuppressWarnings("FieldMayBeFinal") // needs to be mocked for testing
    private RestCountryClient countryApiClient = Feign
            .builder()
            .errorDecoder(new RestErrorDecoder()) // implemented to retry requests in case of failure
            .decoder(new JacksonDecoder())
            .target(RestCountryClient.class, Constants.REST_COUNTRY_URL);

    public void execute(ForwarderType forwarderType) throws AppException {
        List<RestCountryModel> countries;

        try {
            log.debug("Requesting country list from REST API...");

            // request only fields we need
            countries = countryApiClient.getCountries(List.of(
                    RestCountryModel.Fields.name,
                    RestCountryModel.Fields.area,
                    RestCountryModel.Fields.population,
                    RestCountryModel.Fields.region,
                    RestCountryModel.Fields.borders,
                    RestCountryModel.Fields.cca3
            ));

            log.info("Retrieved {} countries from APIs", countries == null ? 0 : countries.size());

        } catch (FeignException e) {
            log.error(e.getMessage(), e);
            throw new AppException("unable to retrieve countries data.");
        }

        // retrieve correct forwarder
        Forwarder forwarder = ForwarderProvider.getForwarder(forwarderType);

        // retrieve processors
        List<Processor> processors = ProcessorProvider.getProcessors();
        log.debug("Found {} processors, starting processes...", processors.size());

        processors.forEach(processor -> {
            processor.process(countries);
            forwarder.forward(processor);
        });

        log.info("Processor execution ended");
    }
}
