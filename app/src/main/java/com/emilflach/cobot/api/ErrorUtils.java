package com.emilflach.cobot.api;

import com.emilflach.cobot.Models.ApiError;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * cobot
 * by Emil on 2015-11-24.
 */
public class ErrorUtils {

    /**
     * Parses the error message from a failed response
     * @param response the response object after failing
     * @param retrofit the corresponding retrofit object
     * @return returns an ApiError object
     */
    public static ApiError parseError(Response response, Retrofit retrofit) {
        Converter<ResponseBody, ApiError> converter =
                retrofit.responseConverter(ApiError.class, new Annotation[0]);

        ApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError();
        }

        return error;
    }
}
