package com.cursospring.libaryapi.service;

import java.util.List;

public interface EmailService {
    void sendMails(String mensagem, List<String> mailsList);
}
