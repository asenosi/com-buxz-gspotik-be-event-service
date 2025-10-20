package com.buxz.gspotik.events.domain.port.inbound;

import com.buxz.gspotik.events.domain.model.Event;
import java.util.Optional;

public interface EventCommandUseCase {
    Event create(Event event);
    Optional<Event> update(Long id, Event event);
    boolean delete(Long id);
}

