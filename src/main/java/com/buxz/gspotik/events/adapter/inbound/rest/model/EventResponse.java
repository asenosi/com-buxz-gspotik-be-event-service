package com.buxz.gspotik.events.adapter.inbound.rest.model;

import java.time.Instant;

public record EventResponse(
        Long id,
        String title,
        String description,
        Instant startTime,
        Instant endTime,
        String location,
        int capacity
) {}

