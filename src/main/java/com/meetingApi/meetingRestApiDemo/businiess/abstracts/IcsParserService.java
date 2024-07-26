package com.meetingApi.meetingRestApiDemo.businiess.abstracts;

import com.meetingApi.meetingRestApiDemo.entities.ics.Event;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public interface IcsParserService {
    List<Event> parseIcsFile(InputStream inputStream) throws IOException;
}
