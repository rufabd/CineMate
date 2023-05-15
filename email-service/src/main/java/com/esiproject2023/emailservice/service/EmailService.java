package com.esiproject2023.emailservice.service;

import com.esiproject2023.emailservice.dto.EmailDto;
import com.esiproject2023.emailservice.model.Email;
import com.esiproject2023.emailservice.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private WebClient.Builder webClient;

    @KafkaListener(topics = {"emailTopic", "discoveryTopic"})
    public EmailDto sendEmail(EmailDto emailDto) {
        Email email = Email.builder()
                .user_email(emailDto.getUser_email())
                .title(emailDto.getTitle())
                .subject(emailDto.getSubject())
                .content(emailDto.getContent())
                .type(emailDto.getType()).build();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("rasulisgandarov35@gmail.com");
        message.setTo(email.getUser_email());
        message.setText(email.getTitle() + "\n\n" + email.getContent());
        message.setSubject(email.getSubject());
        mailSender.send(message);
        if(email.getType().equals("discovery")) {
            webClient.build().post().uri("http://auth-service/auth/update/lastEmail").body(Mono.just(email.getUser_email()), String.class).retrieve().bodyToMono(String.class).block();
        }
        Email result = emailRepository.save(email);

        log.info("Email with id of {} sent successfully...", email.getId());
        return mapToEmailDto(result);
    }

    public EmailDto mapToEmailDto(Email email) {
        return EmailDto.builder()
                .id(email.getId())
                .user_email(email.getUser_email())
                .title(email.getTitle())
                .subject(email.getSubject())
                .content(email.getContent())
                .type(email.getType())
                .build();
    }
}
