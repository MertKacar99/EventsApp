package com.meetingApi.meetingRestApiDemo.businiess.concretes;

import com.meetingApi.meetingRestApiDemo.businiess.abstracts.EventService;
import com.meetingApi.meetingRestApiDemo.dataacess.EventRepository;
import com.meetingApi.meetingRestApiDemo.dataacess.UserRepository;
import com.meetingApi.meetingRestApiDemo.entities.User;
import com.meetingApi.meetingRestApiDemo.entities.ics.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventManager implements EventService {

    private final EventRepository eventRepository;
    private final JavaMailSenderImpl mailSender;
    private final UserRepository userRepository;

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event createEvent(Event event) {
      return eventRepository.save(event);


    }

    @Override
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        event.setUid(id);
        ZonedDateTime dtstamp = event.getDtstamp() != null ? event.getDtstamp() : ZonedDateTime.now();
        event.setOrganizer(eventDetails.getOrganizer());
        event.setStartTime(eventDetails.getStartTime());
        event.setEndTime(eventDetails.getEndTime());
        event.setSummary(eventDetails.getSummary());
        event.setDescription(eventDetails.getDescription());
        event.setLocation(eventDetails.getLocation());
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);

    }

    @Override
    public String genereteICalFile(Event event) {
        ZonedDateTime dtstamp = event.getDtstamp() != null ? event.getDtstamp() : ZonedDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        StringBuilder icalContent = new StringBuilder()
                .append("BEGIN:VCALENDAR\r\n")
                .append("VERSION:2.0\r\n")
                .append("PRODID:-//Your Organization//NONSGML Event//EN\r\n")
                .append("BEGIN:VEVENT\r\n")
                .append("UID:").append(event.getUid()).append("\r\n")
                .append("DTSTAMP:").append(dtstamp.format(dateTimeFormatter)).append("\r\n")
                .append("ORGANIZER;CN=").append(event.getOrganizer()).append(":MAILTO:organizer@example.com\r\n")
                .append("DTSTART:").append(event.getStartTime().format(dateTimeFormatter)).append("\r\n")
                .append("DTEND:").append(event.getEndTime().format(dateTimeFormatter)).append("\r\n")
                .append("SUMMARY:").append(event.getSummary()).append("\r\n")
                .append("DESCRIPTION:").append(event.getDescription()).append("\r\n")
                .append("LOCATION:").append(event.getLocation()).append("\r\n")
                .append("END:VEVENT\r\n")
                .append("END:VCALENDAR");

        return icalContent.toString();
    }

    @Override
    public String genereteAllICalFile(List<Event> events) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        StringBuilder icalBuilder = new StringBuilder();

        icalBuilder.append("BEGIN:VCALENDAR\r\n")
                .append("VERSION:2.0\r\n")
                .append("PRODID:-//Example Corp//NONSGML Event//EN\r\n");

        for (Event event : events) {
            icalBuilder.append("BEGIN:VEVENT\r\n")
                    .append("UID:").append(event.getUid()).append("\r\n")
                    .append("DTSTAMP:").append(event.getDtstamp().format(formatter)).append("\r\n")
                    .append("ORGANIZER;CN=").append(event.getOrganizer()).append(":MAILTO:").append(event.getOrganizer()).append("\r\n")
                    .append("DTSTART:").append(event.getStartTime().format(formatter)).append("\r\n")
                    .append("DTEND:").append(event.getEndTime().format(formatter)).append("\r\n")
                    .append("SUMMARY:").append(event.getSummary()).append("\r\n")
                    .append("DESCRIPTION:").append(event.getDescription()).append("\r\n")
                    .append("LOCATION:").append(event.getLocation()).append("\r\n")
                    .append("END:VEVENT\r\n");
        }

        icalBuilder.append("END:VCALENDAR\r\n");

        return icalBuilder.toString();

    }

    @Override
    public String genereteVCalFile(Event event) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        StringBuilder vcalContent = new StringBuilder()
                .append("BEGIN:VCALENDAR\r\n")
                .append("VERSION:1.0\r\n")
                .append("BEGIN:VEVENT\r\n")
                .append("UID:").append(event.getUid()).append("\r\n")
                .append("DTSTAMP:").append(event.getDtstamp().format(dateTimeFormatter)).append("\r\n")
                .append("ORGANIZER:MAILTO:").append(event.getOrganizer()).append("\r\n")
                .append("DTSTART:").append(event.getStartTime().format(dateTimeFormatter)).append("\r\n")
                .append("DTEND:").append(event.getEndTime().format(dateTimeFormatter)).append("\r\n")
                .append("SUMMARY:").append(event.getSummary()).append("\r\n")
                .append("DESCRIPTION:").append(event.getDescription()).append("\r\n")
                .append("LOCATION:").append(event.getLocation()).append("\r\n")
                .append("END:VEVENT\r\n")
                .append("END:VCALENDAR");

        return vcalContent.toString();
    }


}
