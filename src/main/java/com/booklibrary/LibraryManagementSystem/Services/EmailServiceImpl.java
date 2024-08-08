package com.booklibrary.LibraryManagementSystem.Services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.booklibrary.LibraryManagementSystem.Data.Entities.EmailConfirmationToken;
import com.booklibrary.LibraryManagementSystem.Data.Entities.ResetPasswordsTokens;
import com.booklibrary.LibraryManagementSystem.Data.Entities.TwoFactorCode;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl {

    private final JavaMailSender _sender;

    public EmailServiceImpl(JavaMailSender sender) {
        _sender = sender;
    }

    public void sendConfirmationEmail(EmailConfirmationToken e) throws MessagingException {
        //MIME - HTML message
        MimeMessage message = _sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(e.getUser().getEmail());
        helper.setSubject("Confirm you E-Mail - MFA Application Registration");
        helper.setText("<html>" +
                        "<body>" +
                        "<h2>Dear "+ e.getUser().getFullName() + ",</h2>"
                        + "<br/> We're excited to have you get started. " +
                        "Please click on below link to confirm your account."
                        + "<br/> "  + generateConfirmationLink(e.getToken(), e.getUser().getUserName())+"" +
                        "<br/> Regards,<br/>" +
                        "MFA Registration team" +
                        "</body>" +
                        "</html>"
                , true);

        _sender.send(message);
    }

    private String generateConfirmationLink(String token, String userName){
        return "<a href=http://localhost:8080/api/patron/confirm-email?token="+token+"&userName="+userName+">Confirm Email</a>";
    }


    public void sendTwoFactorCode(TwoFactorCode e) throws MessagingException {
        //MIME - HTML message
        MimeMessage message = _sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(e.getUser().getEmail());
        helper.setSubject("Two Factor Code - MFA Application Registration");
        helper.setText("<html>" +
                        "<body>" +
                        "<h2>Dear "+ e.getUser().getFullName() + ",</h2>"
                        + "<br/> We're excited to have you get started. " +
                        "This is your code."
                        + "<br/> "  + e.getCode() +"" +
                        "<br/> Regards,<br/>" +
                        "MFA Registration team" +
                        "</body>" +
                        "</html>"
                , true);

        _sender.send(message);
    }

    public void sendResetPasswordCode(ResetPasswordsTokens e) throws MessagingException {
        //MIME - HTML message
        MimeMessage message = _sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(e.getUser().getEmail());
        helper.setSubject("Reset Password Code - MFA Application Registration");
        helper.setText("<html>" +
                        "<body>" +
                        "<h2>Dear "+ e.getUser().getFullName() + ",</h2>"
                        + "<br/> We're excited to have you get started. " +
                        "This is your code."
                        + "<br/> "  + e.getCode() +"" +
                        "<br/> Regards,<br/>" +
                        "MFA Registration team" +
                        "</body>" +
                        "</html>"
                , true);

        _sender.send(message);
    }

}
