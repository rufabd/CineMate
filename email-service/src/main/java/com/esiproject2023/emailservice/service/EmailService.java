package com.esiproject2023.emailservice.service;

import com.esiproject2023.emailservice.dto.EmailDto;
import com.esiproject2023.emailservice.model.Email;
import com.esiproject2023.emailservice.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepository emailRepository;

    public EmailDto sendEmail(EmailDto emailDto) {
        Email email = Email.builder()
                .user_email(emailDto.getUser_email())
                .title(emailDto.getTitle())
                .subject(emailDto.getSubject())
                .content(emailDto.getContent()).build();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("platformcinemate@gmail.com");
        message.setTo(email.getUser_email());
        message.setText(email.getTitle() + "\n\n" + email.getContent());
        message.setSubject(email.getSubject());
        mailSender.send(message);

//        Null check here. It doesn't save email yet, because of the null exception.
        System.out.println(email);
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
                .content(email.getContent()).build();
    }
}
