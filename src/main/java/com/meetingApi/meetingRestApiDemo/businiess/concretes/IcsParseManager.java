package com.meetingApi.meetingRestApiDemo.businiess.concretes;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.meetingApi.meetingRestApiDemo.businiess.abstracts.IcsParserService;
import com.meetingApi.meetingRestApiDemo.entities.ics.Event;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class IcsParseManager implements IcsParserService {
    @Override
    public List<Event> parseIcsFile(InputStream inputStream) throws IOException {
        List<Event> events = new ArrayList<>();

        ICalendar ical = Biweekly.parse(inputStream).first();
        for (VEvent vEvent : ical.getEvents()) {
            Event event = new Event();
            event.setDtstamp(ZonedDateTime.ofInstant(vEvent.getDateTimeStamp().getValue().toInstant(), ZoneId.systemDefault()));
            event.setOrganizer(vEvent.getOrganizer().getCommonName());
            event.setStartTime(ZonedDateTime.ofInstant(vEvent.getDateStart().getValue().toInstant(), ZoneId.systemDefault()));
            event.setEndTime(ZonedDateTime.ofInstant(vEvent.getDateEnd().getValue().toInstant(), ZoneId.systemDefault()));
            event.setSummary(vEvent.getSummary().getValue());
            event.setDescription(vEvent.getDescription().getValue());
            event.setLocation(vEvent.getLocation().getValue());

            events.add(event);
        }


        return events;
    }
}
