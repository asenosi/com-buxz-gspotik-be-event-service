package com.buxz.gspotik.events;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event extends PanacheEntity {

    @Column(nullable = false)
    public String title;

    @Column(length = 2048)
    public String description;

    @Column(name = "start_time", nullable = false)
    public LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    public LocalDateTime endTime;

    @Column(nullable = false)
    public String location;

    @Column(nullable = false)
    public int capacity;
}
