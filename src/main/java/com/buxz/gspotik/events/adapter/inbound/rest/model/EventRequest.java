package com.buxz.gspotik.events.adapter.inbound.rest.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record EventRequest(
        @NotBlank String title,
        String description,
        @NotNull @FutureOrPresent Instant startTime,
        @NotNull @Future Instant endTime,
        @NotBlank String location,
        @Min(1) int capacity
) {}

