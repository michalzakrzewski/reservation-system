package com.zakrzewski.reservationsystem.api;

import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public abstract class ApiBase {
    private static final Logger LOG = LoggerFactory.getLogger(ApiBase.class);

    protected <T> ApiResponse<T> callWithExceptionHandling(final Call<T> call) {
        final ApiResponse.ApiResponseBuilder<T> responseBuilder = ApiResponse.builder();
        try {
            final Response<T> response = call.execute();
            responseBuilder.statusCode(response.code());
            if(response.isSuccessful()) {
                LOG.debug("API call successful for: {}", call.request().url());
                return responseBuilder
                        .success(true)
                        .data(response.body())
                        .build();
            } else {
                final ResponseBody responseBody = response.errorBody();
                final String errorMsg = extractErrorBody(responseBody);

                LOG.warn("API call failed with status {} for: {}. Error: {}", response.code(), call.request().url(), errorMsg);
                return responseBuilder
                        .success(false)
                        .errorDetails(errorMsg)
                        .build();
            }
        } catch (IOException e) {
            LOG.error("REST method I/O exception for {}: {}", call.request().url(), e.getMessage(), e);
            return responseBuilder
                    .success(false)
                    .errorDetails("Network or I/O error: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected exception during REST call for: {}", call.request().url(), e);
            return responseBuilder
                    .success(false)
                    .errorDetails("Unexpected internal error: " + e.getMessage())
                    .build();
        }
    }

    private String extractErrorBody(ResponseBody responseBody) throws Exception {
        if (responseBody != null) {
            try {
                return responseBody.string();
            } catch (IOException ioException) {
                LOG.error("Could not read error body", ioException);
                throw new Exception("Error body not readable.");
            }
        }
        LOG.error("Unknown error (No error body available)");
        throw new Exception("No error body available");
    }
}
