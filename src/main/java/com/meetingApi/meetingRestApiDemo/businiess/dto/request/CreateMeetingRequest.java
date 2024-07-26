package com.meetingApi.meetingRestApiDemo.businiess.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class CreateMeetingRequest {
 @NotEmpty
    private String title;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Long> participants;


}
