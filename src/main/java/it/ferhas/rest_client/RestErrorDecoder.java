package it.ferhas.rest_client;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

/**
 * Custom error decoder that triggers a retry for all non 200 HTTP responses
 */
public class RestErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return new RetryableException(response.status(), response.reason(), response.request().httpMethod(), null, response.request());
    }
}
