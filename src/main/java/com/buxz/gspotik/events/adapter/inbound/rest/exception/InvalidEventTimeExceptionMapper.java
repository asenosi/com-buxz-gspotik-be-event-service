package com.buxz.gspotik.events.adapter.inbound.rest.exception;

import com.buxz.gspotik.events.domain.exception.InvalidEventTimeException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class InvalidEventTimeExceptionMapper implements ExceptionMapper<InvalidEventTimeException> {
    @Override
    public Response toResponse(InvalidEventTimeException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(Map.of(
                        "error", "invalid_event_time",
                        "message", exception.getMessage()
                ))
                .build();
    }
}

