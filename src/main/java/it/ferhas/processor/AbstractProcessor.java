package it.ferhas.processor;

import it.ferhas.rest_client.model.RestCountryModel;
import lombok.Getter;

import java.util.List;

public abstract class AbstractProcessor implements Processor {
    @Getter
    List<RestCountryModel> data;

    protected abstract List<RestCountryModel> doProcess(List<RestCountryModel> countries);

    @Override
    public final void process(List<RestCountryModel> countries) {
        this.data = doProcess(countries);
    }
}
