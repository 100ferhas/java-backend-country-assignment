package it.ferhas.processor;

import it.ferhas.rest_client.model.RestCountryModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class AbstractProcessor implements Processor {
    @Getter
    List<RestCountryModel> data;

    protected abstract List<RestCountryModel> doProcess(List<RestCountryModel> countries);

    @Override
    public final void process(List<RestCountryModel> countries) {
        log.info("Processor '{}' started", this.getClass().getSimpleName());

        if (countries == null) {
            this.data = null;
            log.debug("No countries to process.");
        } else {
            this.data = doProcess(countries);
        }

        log.info("Processor '{}' ended successfully", this.getClass().getSimpleName());
    }
}
