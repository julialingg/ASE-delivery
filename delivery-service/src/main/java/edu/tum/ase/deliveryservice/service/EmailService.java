package edu.tum.ase.deliveryservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender MailSender;


    public void sendEmail(String uName, String Subject, String msg) {
        SimpleMailMessage simpleMailMsg = new SimpleMailMessage();
        simpleMailMsg.setTo(uName);
        simpleMailMsg.setSubject(Subject);
        simpleMailMsg.setText(msg);
        MailSender.send(simpleMailMsg);
    }

}
