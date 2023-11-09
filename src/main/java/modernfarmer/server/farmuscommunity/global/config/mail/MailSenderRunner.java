package modernfarmer.server.farmuscommunity.global.config.mail;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;



@Component
@RequiredArgsConstructor
public class MailSenderRunner  {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.to}")
    public String to;



    public void sendEmail(String subject, String text) throws Exception {
        MimeMessage m = mailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");
        h.setFrom(from);
        h.setTo(to);
        h.setSubject(subject);
        h.setText(text);
        mailSender.send(m);
    }
}