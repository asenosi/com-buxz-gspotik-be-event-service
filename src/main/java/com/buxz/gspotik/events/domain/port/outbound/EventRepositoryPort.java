package com.buxz.gspotik.events.domain.port.outbound;

import com.buxz.gspotik.events.domain.model.Event;
import io.quarkus.panache.common.Sort;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryPort {
    List<Event> listAll();
    List<Event> listFiltered(String q, String location, Instant from, Instant to, Integer page, Integer size, Sort sort);
    long countFiltered(String q, String location, Instant from, Instant to);
    Optional<Event> findById(Long id);
    Event save(Event event);
    Optional<Event> update(Long id, Event event);
    boolean delete(Long id);
}

