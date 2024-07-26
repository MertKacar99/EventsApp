package com.meetingApi.meetingRestApiDemo.businiess.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeetingResponse {
    private  Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<UserResponse> participants;
}
