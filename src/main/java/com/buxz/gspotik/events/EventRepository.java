package com.buxz.gspotik.events;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EventRepository implements PanacheRepository<Event> {

    public List<Event> listUpcoming() {
        return listAll(Sort.by("startTime"));
    }
}
