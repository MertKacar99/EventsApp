package com.meetingApi.meetingRestApiDemo.businiess.concretes;

import com.meetingApi.meetingRestApiDemo.businiess.abstracts.EmailService;
import com.meetingApi.meetingRestApiDemo.businiess.mail.MailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailManager  implements EmailService  {

    private  final JavaMailSender mailSender;
    @Override
    public void sendSimpleMail(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());
        mailSender.send(message);
    }
}
