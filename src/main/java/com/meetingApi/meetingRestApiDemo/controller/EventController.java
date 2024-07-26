package com.meetingApi.meetingRestApiDemo.controller;


import com.meetingApi.meetingRestApiDemo.businiess.abstracts.EventService;
import com.meetingApi.meetingRestApiDemo.businiess.abstracts.IcsParserService;
import com.meetingApi.meetingRestApiDemo.entities.ics.Event;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final IcsParserService icsParserService;
    public EventController(EventService eventService, IcsParserService icsParserService) {
        this.eventService = eventService;
        this.icsParserService = icsParserService;
    }

    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(event -> ResponseEntity.ok().body(event))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadICalFile(@PathVariable Long id) {
        Event event = eventService.getEventById(id).orElseThrow(() -> new RuntimeException("Event not found!"));
        String icalContent = eventService.genereteICalFile(event);

        byte[] output = icalContent.getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=event.ics");

        return ResponseEntity.ok()
                .headers(headers)
                .body(output);
    }

    @PostMapping("/add")
    public ResponseEntity<byte[]> addEventAndDownloadICalFile(@RequestBody Event event) {
        // Gelen zamanı Türkiye saat dilimi olarak kabul et
        ZonedDateTime startTimeTR = event.getStartTime().withZoneSameInstant(ZoneId.of("Europe/Istanbul"));
        ZonedDateTime endTimeTR = event.getEndTime().withZoneSameInstant(ZoneId.of("Europe/Istanbul"));

        event.setStartTime(startTimeTR);
        event.setEndTime(endTimeTR);

        // dtstamp'i burada manuel olarak ayarlayabilirsiniz
        event.setDtstamp(ZonedDateTime.now());

        Event createdEvent = eventService.createEvent(event);
        String icalContent = eventService.genereteICalFile(createdEvent);

        byte[] output = icalContent.getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=event.ics");

        return ResponseEntity.ok()
                .headers(headers)
                .body(output);
    }

    @GetMapping("/download/all")
    public  ResponseEntity<byte[]> downloadICalFileForAllEvents() {
        List<Event> events = eventService.getAllEvents();
        String icalContent = eventService.genereteAllICalFile(events);

        byte[] output = icalContent.getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=events.ics");

        return ResponseEntity.ok()
                .headers(headers)
                .body(output);
    }

    @PostMapping("/event/update/{id}")
    public ResponseEntity<Optional<Event>> updateEvent(@RequestBody Event event, @PathVariable Long id) {
       this.eventService.updateEvent(id,event);
       return  ResponseEntity.ok().body(eventService.getEventById(id));

    }
    @PostMapping("/event/import")
    public ResponseEntity<String> importEvents(@RequestParam("file")MultipartFile file){
        try(InputStream inputStream = file.getInputStream()){
            List<Event> events = icsParserService.parseIcsFile(inputStream);
            for (Event event : events) {
                eventService.createEvent(event);
            }
            return ResponseEntity.ok("Events imported successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import events: " + e.getMessage());
        }
    }

    @GetMapping("/download/gmail/{id}")
    public  ResponseEntity<byte[]> downloadVCalFile(@PathVariable Long id) {
        Event event = eventService.getEventById(id).orElseThrow(() -> new RuntimeException("Event not found!"));
        String vcalContent = eventService.genereteVCalFile(event);

        byte[] output = vcalContent.getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=event.vcs");

        return ResponseEntity.ok()
                .headers(headers)
                .body(output);
    }


}

