package com.buxz.gspotik.events;

import com.buxz.gspotik.events.dto.EventRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventService {

    private final EventRepository repository;

    @Inject
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> listEvents() {
        return repository.listUpcoming();
    }

    public Optional<Event> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Transactional
    public Event create(EventRequest request) {
        validateTimeRange(request);
        Event event = new Event();
        applyRequest(event, request);
        repository.persist(event);
        return event;
    }

    @Transactional
    public Optional<Event> update(Long id, EventRequest request) {
        validateTimeRange(request);
        return repository.findByIdOptional(id).map(existing -> {
            applyRequest(existing, request);
            return existing;
        });
    }

    @Transactional
    public boolean delete(Long id) {
        return repository.deleteById(id);
    }

    private void validateTimeRange(EventRequest request) {
        if (request.endTime().isBefore(request.startTime())) {
            throw new WebApplicationException(
                    "Event end time must be after the start time",
                    Response.Status.BAD_REQUEST
            );
        }
    }

    private void applyRequest(Event event, EventRequest request) {
        event.title = request.title();
        event.description = request.description();
        event.startTime = request.startTime();
        event.endTime = request.endTime();
        event.location = request.location();
        event.capacity = request.capacity();
    }
}
