package com.buxz.gspotik.events.domain.service;

import com.buxz.gspotik.events.domain.model.Event;
import com.buxz.gspotik.events.domain.port.inbound.EventCommandUseCase;
import com.buxz.gspotik.events.domain.port.inbound.EventQueryUseCase;
import com.buxz.gspotik.events.domain.port.outbound.EventRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import com.buxz.gspotik.events.domain.exception.InvalidEventTimeException;
import io.quarkus.panache.common.Sort;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventService implements EventQueryUseCase, EventCommandUseCase {

    private final EventRepositoryPort repository;

    @Inject
    public EventService(EventRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Event> listAll() { return repository.listAll(); }

    @Override
    public List<Event> search(String q, String location, Instant from, Instant to, Integer page, Integer size, Sort sort) {
        return repository.listFiltered(q, location, from, to, page, size, sort);
    }

    @Override
    public long count(String q, String location, Instant from, Instant to) {
        return repository.countFiltered(q, location, from, to);
    }

    @Override
    public Optional<Event> findById(Long id) { return repository.findById(id); }

    @Override
    @Transactional
    public Event create(Event event) {
        validateTimeRange(event);
        return repository.save(event);
    }

    @Override
    @Transactional
    public Optional<Event> update(Long id, Event event) {
        validateTimeRange(event);
        return repository.update(id, event);
    }

    @Override
    @Transactional
    public boolean delete(Long id) { return repository.delete(id); }

    private void validateTimeRange(Event event) {
        if (event.getEndTime().isBefore(event.getStartTime())) {
            throw new InvalidEventTimeException("Event end time must be after the start time");
        }
    }
}
