package com.bmiservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

/**
 * Error body returned by every handled exception.
 *
 * @param fieldErrors validation messages keyed by field name; omitted when empty
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorDetails(
        Instant timestamp,
        int status,
        String message,
        String path,
        Map<String, String> fieldErrors
) {

    public ErrorDetails(int status, String message, String path) {
        this(Instant.now(), status, message, path, Map.of());
    }
}
