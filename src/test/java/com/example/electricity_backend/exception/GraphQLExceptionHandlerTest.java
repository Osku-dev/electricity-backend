package com.example.electricity_backend.exception;

import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GraphQLExceptionHandlerTest {

    private DataFetchingEnvironment env;

    @BeforeEach
    void setUp() {
        env = mock(DataFetchingEnvironment.class);

        ExecutionStepInfo executionStepInfo = mock(ExecutionStepInfo.class);
        ResultPath path = ResultPath.parse("/test/path");
        Field field = mock(Field.class);

        when(env.getExecutionStepInfo()).thenReturn(executionStepInfo);
        when(executionStepInfo.getPath()).thenReturn(path);
        when(env.getField()).thenReturn(field);
        when(field.getSourceLocation()).thenReturn(new SourceLocation(1, 1));
    }

    @Test
    void shouldReturnDevErrorInDevProfile() {
        GraphQLExceptionHandler handler = new GraphQLExceptionHandler("dev");

        RuntimeException ex = new RuntimeException("Something broke");

        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertEquals("DEV_ERROR", error.getExtensions().get("code"));
        assertTrue(error.getMessage().contains("RuntimeException"));
    }

    @Test
    void shouldReturnBadRequestForIllegalArgumentExceptionInProd() {
        GraphQLExceptionHandler handler = new GraphQLExceptionHandler("prod");

        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");

        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertEquals("BAD_REQUEST", error.getExtensions().get("code"));
        assertEquals("Invalid request parameters", error.getMessage());
    }

    @Test
    void shouldReturnInternalErrorForOtherExceptionsInProd() {
        GraphQLExceptionHandler handler = new GraphQLExceptionHandler("prod");

        RuntimeException ex = new RuntimeException("Database crashed");

        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertEquals("INTERNAL_ERROR", error.getExtensions().get("code"));
        assertEquals("Internal server error", error.getMessage());
    }
}
