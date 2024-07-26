package com.meetingApi.meetingRestApiDemo.businiess.abstracts;

import com.meetingApi.meetingRestApiDemo.businiess.mail.MailBody;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendSimpleMail(MailBody mailBody);

}
