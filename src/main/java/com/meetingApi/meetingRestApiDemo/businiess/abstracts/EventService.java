package com.meetingApi.meetingRestApiDemo.businiess.abstracts;

import com.meetingApi.meetingRestApiDemo.entities.User;
import com.meetingApi.meetingRestApiDemo.entities.ics.Event;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public interface EventService {
    // LİSTELEME
    List<Event> getAllEvents();
    Optional<Event> getEventById(Long id);
    Event createEvent(Event event);
    Event updateEvent(Long id, Event eventDetails);
    void deleteEvent(Long id);
    String genereteICalFile(Event event);
    String genereteAllICalFile(List<Event> events);

    //vcal
    String genereteVCalFile(Event event);





}
