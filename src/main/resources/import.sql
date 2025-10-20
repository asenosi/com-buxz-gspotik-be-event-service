insert into events (id, title, description, start_time, end_time, location, capacity) values
    (1, 'Product Launch', 'Launch event for the new platform release.', '2099-01-10T09:00:00', '2099-01-10T12:00:00', 'Main Auditorium', 250);
insert into events (id, title, description, start_time, end_time, location, capacity) values
    (2, 'Community Meetup', 'Monthly networking event for community members.', '2099-02-05T18:00:00', '2099-02-05T20:00:00', 'Innovation Hub', 80);
alter sequence events_seq restart with 3;
