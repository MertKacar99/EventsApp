package com.meetingApi.meetingRestApiDemo.businiess.mail;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {

}
