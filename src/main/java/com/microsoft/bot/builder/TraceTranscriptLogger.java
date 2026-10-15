// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.bot.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.microsoft.bot.connector.Async;
import com.microsoft.bot.schema.Activity;

import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a transcript logger that writes activities to a
 * <see cref="Trace"/> object.
 */
public class TraceTranscriptLogger implements TranscriptLogger {
    /**
     * It's a logger.
     */
  private static Logger log = LogManager.getLogger();


    /**
     * For outputting Activity as JSON.
     */
    // https://github.com/FasterXML/jackson-databind/wiki/Serialization-Features
    private static ObjectMapper mapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .findAndRegisterModules();

    /**
     * Log an activity to the transcript.
     *
     * @param activity The activity to transcribe.
     * @return A task that represents the work queued to execute.
     */
    @Override
    public CompletableFuture<Void> logActivity(Activity activity) {
        if (activity == null) {
            return Async.completeExceptionally(new IllegalArgumentException("Activity"));
        }

        String event = null;
        try {
            event = mapper.writeValueAsString(activity);
        } catch (JsonProcessingException e) {
            log.error("logActivity", e);
            CompletableFuture.completedFuture(null);
        }
        log.info(event);

        return CompletableFuture.completedFuture(null);
    }
}
