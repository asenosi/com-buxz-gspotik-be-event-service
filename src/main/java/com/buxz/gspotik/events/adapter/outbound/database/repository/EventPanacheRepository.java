package com.buxz.gspotik.events.adapter.outbound.database.repository;

import com.buxz.gspotik.events.adapter.outbound.database.entity.EventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class EventPanacheRepository implements PanacheRepository<EventEntity> {

    public List<EventEntity> listUpcoming() {
        return listAll(Sort.by("startTime"));
    }

    public List<EventEntity> listFiltered(
            String q,
            String location,
            Instant from,
            Instant to,
            Integer page,
            Integer size,
            Sort sort
    ) {
        StringBuilder query = new StringBuilder("1=1");
        java.util.Map<String, Object> params = new java.util.HashMap<>();

        if (q != null && !q.isBlank()) {
            query.append(" and (lower(title) like :q or lower(description) like :q)");
            params.put("q", "%" + q.toLowerCase() + "%");
        }
        if (location != null && !location.isBlank()) {
            query.append(" and lower(location) = :location");
            params.put("location", location.toLowerCase());
        }
        if (from != null) {
            query.append(" and startTime >= :from");
            params.put("from", from);
        }
        if (to != null) {
            query.append(" and endTime <= :to");
            params.put("to", to);
        }

        var panacheQuery = find(query.toString(), sort == null ? Sort.by("startTime") : sort, params);
        if (page != null && size != null && page >= 0 && size > 0) {
            panacheQuery.page(Page.of(page, size));
        }
        return panacheQuery.list();
    }

    public long countFiltered(String q, String location, Instant from, Instant to) {
        StringBuilder query = new StringBuilder("1=1");
        java.util.Map<String, Object> params = new java.util.HashMap<>();

        if (q != null && !q.isBlank()) {
            query.append(" and (lower(title) like :q or lower(description) like :q)");
            params.put("q", "%" + q.toLowerCase() + "%");
        }
        if (location != null && !location.isBlank()) {
            query.append(" and lower(location) = :location");
            params.put("location", location.toLowerCase());
        }
        if (from != null) {
            query.append(" and startTime >= :from");
            params.put("from", from);
        }
        if (to != null) {
            query.append(" and endTime <= :to");
            params.put("to", to);
        }
        return count(query.toString(), params);
    }
}
