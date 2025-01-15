package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;


    public void sendEmail(SentimentData sentimentData) {
        emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
    }
}