package com.buxz.gspotik.events;

import com.buxz.gspotik.events.adapter.outbound.database.entity.EventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventRepository implements PanacheRepository<EventEntity> {
    // This repository exists solely for test cleanup via deleteAll()
}

