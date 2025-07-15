package ru.tech.telegrambot.exception;

import java.util.Collections;
import java.util.Map;

/**
 * Base class for all business-related exceptions.
 * Contains immutable context data for error handling and logging.
 */
public abstract class BusinessException extends RuntimeException {
    private final transient Map<String, Object> context;

    /**
     * Creates exception with message and context.
     *
     * @param message human-readable error description
     * @param context additional error data (can be null)
     */
    protected BusinessException(String message, Map<String, Object> context) {
        this(message, context, null);
    }

    /**
     * Creates exception with message, context and cause.
     *
     * @param message human-readable error description
     * @param context additional error data (can be null)
     * @param cause   root exception
     */
    protected BusinessException(String message, Map<String, Object> context, Throwable cause) {
        super(message, cause);
        this.context = context == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(context);
    }


    /**
     * @return immutable exception context.
     */
    public Map<String, Object> getContext() {
        return context;
    }
}
