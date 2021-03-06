package com.gitlab.pedrioko.services;

import com.gitlab.pedrioko.domain.EmailAccount;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * The Class SmtpMailSender.
 */

public interface MailService {

    void send(String subject, String body, String... to);

    void send(String subject, String body, List<String> tos, Map<String, File> attachment, Map<String, Object> vars);

    JavaMailSender createEmailSender();

    EmailAccount getEmailAccount();
}
