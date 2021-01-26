package com.cursospring.libaryapi.service.impl;

import com.cursospring.libaryapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${application.mail.default-remetent}")
    private String remetent;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMails(String mensagem, List<String> mailsList) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String[] mails = mailsList.toArray(new String[mailsList.size()]);

        mailMessage.setFrom(remetent);
        mailMessage.setSubject("Livro com emprestimo atrasado !");
        mailMessage.setText(mensagem);

        mailMessage.setTo(mails);
        javaMailSender.send(mailMessage);
    }
}
