package com.buxz.gspotik.events.adapter.outbound.database.mapper;

import com.buxz.gspotik.events.adapter.outbound.database.entity.EventEntity;
import com.buxz.gspotik.events.domain.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EventPersistenceMapper {
    EventEntity toEntity(Event domain);
    Event toDomain(EventEntity entity);
}

