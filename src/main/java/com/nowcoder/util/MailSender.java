package com.nowcoder.util;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    private Session session;

    private Message message;
    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            String result = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            String nick = MimeUtility.encodeText("Dapeng");
            message.setFrom(new InternetAddress(nick + "<tongxupeng@126.com>"));//发件人邮箱
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(to)); //收件人邮箱(我的QQ邮箱)
            message.setSubject(subject);
            message.setText(result);
            message.saveChanges();
            Transport transport = session.getTransport();
            transport.connect("tongxupeng@126.com","xxr5201314");//发件人邮箱,授权码(可以在邮箱设置中获取到授权码的信息)
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("邮件发送成功...");
            transport.close();
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host","smtp.126.com");// smtp服务器地址
        session = Session.getInstance(props);
        session.setDebug(true);
        message = new MimeMessage(session);
    }
}
