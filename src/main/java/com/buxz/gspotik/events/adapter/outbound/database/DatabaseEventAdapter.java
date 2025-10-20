package com.buxz.gspotik.events.adapter.outbound.database;

import com.buxz.gspotik.events.adapter.outbound.database.entity.EventEntity;
import com.buxz.gspotik.events.adapter.outbound.database.mapper.EventPersistenceMapper;
import com.buxz.gspotik.events.domain.model.Event;
import com.buxz.gspotik.events.domain.port.outbound.EventRepositoryPort;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.buxz.gspotik.events.adapter.outbound.database.repository.EventPanacheRepository;

@ApplicationScoped
public class DatabaseEventAdapter implements EventRepositoryPort {

    private final EventPanacheRepository repository;
    private final EventPersistenceMapper mapper;

    @Inject
    public DatabaseEventAdapter(EventPanacheRepository repository, EventPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Event> listAll() {
        return repository.listUpcoming().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Event> listFiltered(String q, String location, Instant from, Instant to, Integer page, Integer size, Sort sort) {
        return repository.listFiltered(q, location, from, to, page, size, sort).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public long countFiltered(String q, String location, Instant from, Instant to) {
        return repository.countFiltered(q, location, from, to);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return repository.findByIdOptional(id).map(mapper::toDomain);
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = mapper.toEntity(event);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Event> update(Long id, Event event) {
        return repository.findByIdOptional(id).map(existing -> {
            existing.title = event.getTitle();
            existing.description = event.getDescription();
            existing.startTime = event.getStartTime();
            existing.endTime = event.getEndTime();
            existing.location = event.getLocation();
            existing.capacity = event.getCapacity();
            return mapper.toDomain(existing);
        });
    }

    @Override
    public boolean delete(Long id) {
        return repository.deleteById(id);
    }

    // Mapping handled by MapStruct mapper
}
