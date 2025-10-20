package com.buxz.gspotik.events.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record EventRequest(
        @NotBlank String title,
        String description,
        @NotNull @FutureOrPresent LocalDateTime startTime,
        @NotNull @Future LocalDateTime endTime,
        @NotBlank String location,
        @Min(1) int capacity
) {
}
