package com.buxz.gspotik.events.adapter.inbound.rest;

import com.buxz.gspotik.events.adapter.inbound.rest.mapper.EventRestMapper;
import com.buxz.gspotik.events.adapter.inbound.rest.model.EventRequest;
import com.buxz.gspotik.events.adapter.inbound.rest.model.EventResponse;
import com.buxz.gspotik.events.domain.model.Event;
import com.buxz.gspotik.events.domain.port.inbound.EventCommandUseCase;
import com.buxz.gspotik.events.domain.port.inbound.EventQueryUseCase;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventController {

    private final EventQueryUseCase query;
    private final EventCommandUseCase command;
    private final EventRestMapper mapper;

    @Inject
    public EventController(EventQueryUseCase query, EventCommandUseCase command, EventRestMapper mapper) {
        this.query = query;
        this.command = command;
        this.mapper = mapper;
    }

    @GET
    public List<EventResponse> list(
            @QueryParam("q") String q,
            @QueryParam("location") String location,
            @QueryParam("from") Instant from,
            @QueryParam("to") Instant to,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size
    ) {
        var sort = Sort.by("startTime");
        List<Event> events = (q != null || location != null || from != null || to != null || page != null || size != null)
                ? query.search(q, location, from, to, page, size, sort)
                : query.listAll();
        return events.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        return query.findById(id)
                .map(e -> Response.ok(mapper.toResponse(e)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response create(@Valid EventRequest request, @Context UriInfo uriInfo) {
        Event created = command.create(mapper.toDomain(request));
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(created.getId())).build();
        return Response.created(location).entity(mapper.toResponse(created)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid EventRequest request) {
        return command.update(id, mapper.toDomain(request))
                .map(e -> Response.ok(mapper.toResponse(e)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = command.delete(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    // Mapping moved to MapStruct mapper
}
