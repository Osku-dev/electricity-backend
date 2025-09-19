package com.example.electricity_backend.exception;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;



@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {

        String message;
        String code;

        if ("dev".equals(activeProfile)) {
            message = ex.toString(); 
            code = "DEV_ERROR";
        } else {
            if (ex instanceof IllegalArgumentException) {
                message = "Invalid request parameters";
                code = "BAD_REQUEST";
            } else {
                message = "Internal server error";
                code = "INTERNAL_ERROR";
            }
        }
        

        return GraphqlErrorBuilder.newError()
                .message(message)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(Map.of("code", code))
                .build();
    }
}