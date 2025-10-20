package com.buxz.gspotik.events.domain.port.inbound;

import com.buxz.gspotik.events.domain.model.Event;
import io.quarkus.panache.common.Sort;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventQueryUseCase {
    List<Event> listAll();
    List<Event> search(String q, String location, Instant from, Instant to, Integer page, Integer size, Sort sort);
    long count(String q, String location, Instant from, Instant to);
    Optional<Event> findById(Long id);
}

