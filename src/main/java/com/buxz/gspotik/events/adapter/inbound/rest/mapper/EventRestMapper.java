package com.buxz.gspotik.events.adapter.inbound.rest.mapper;

import com.buxz.gspotik.events.adapter.inbound.rest.model.EventRequest;
import com.buxz.gspotik.events.adapter.inbound.rest.model.EventResponse;
import com.buxz.gspotik.events.domain.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EventRestMapper {
    @Mapping(target = "id", ignore = true)
    Event toDomain(EventRequest request);
    EventResponse toResponse(Event event);
}
