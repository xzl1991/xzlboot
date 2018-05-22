package com.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.apache.commons.mail.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by xlizy on 2017/4/19.
 */
@Component
public class EMailUtils {

    @Value("${email.hostName}")
    private String hostName;

    @Value("${email.smtpPort}")
    private Integer smtpPort;

    @Value("${email.userName}")
    private String userName;

    @Value("${email.password}")
    private String password;

    @Value("${email.SSLOnConnect}")
    private Boolean SSLOnConnect;

    @Value("${email.from}")
    private String from;

    @Value("${email.group.developers}")
    private String groupDevelopers;

    @Value("${email.group.olg}") @Getter
    private String groupOLG;

    @Value("${email.enabled}") @Getter
    private String enabled;

    /**
     * 简单邮件
     * */
    public void sendSimpleTextEmail(JSONObject object) throws EmailException {
        if (!Boolean.TRUE.toString().equalsIgnoreCase(enabled)) {
            return;
        }
        Email email = new SimpleEmail();
        email.setHostName(hostName);
        email.setSmtpPort(smtpPort);
        email.setAuthenticator(new DefaultAuthenticator(userName,password));
        email.setSSLOnConnect(SSLOnConnect);
        email.setFrom(from);
        email.setSubject(object.getString("Subject"));
        email.setMsg(object.getString("Msg"));
        if (StringUtils.isNotEmpty(object.getString("To"))) {
            for (String to : object.getString("To").split(",")) {
                email.addTo(to);
            }
        }
        if (StringUtils.isNotEmpty(object.getString("Cc"))) {
            for (String cc : object.getString("Cc").split(",")) {
                email.addCc(cc);
            }
        }
        if (StringUtils.isNotEmpty(object.getString("Bcc"))) {
            for (String bcc : object.getString("Bcc").split(",")) {
                email.addBcc(bcc);
            }
        }
        email.send();
    }

    /**
     * 带附件邮件
     * */
    public void sendEmailsWithAttachments(JSONObject object) throws EmailException {
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(hostName);
        email.setSmtpPort(smtpPort);
        email.setAuthenticator(new DefaultAuthenticator(userName,password));
        email.setSSLOnConnect(SSLOnConnect);
        email.setFrom(from);
        email.setSubject(object.getString("Subject"));
        email.setMsg(object.getString("Msg"));
        if (StringUtils.isNotEmpty(object.getString("To"))) {
            for (String to : object.getString("To").split(",")) {
                email.addTo(to);
            }
        }
        if (StringUtils.isNotEmpty(object.getString("Cc"))) {
            for (String cc : object.getString("Cc").split(",")) {
                email.addCc(cc);
            }
        }
        if (StringUtils.isNotEmpty(object.getString("Bcc"))) {
            for (String bcc : object.getString("Bcc").split(",")) {
                email.addBcc(bcc);
            }
        }

        //Demo...
        //EmailAttachment attachment = new EmailAttachment();
        //attachment.setPath("/Users/xlizy/Pictures/icon/xlizy1.png");
        //attachment.setDisposition(EmailAttachment.ATTACHMENT);
        //attachment.setDescription("Picture of xlizy1");
        //attachment.setName("xlizy1");
        //email.attach(attachment);
        //EmailAttachment attachment2 = new EmailAttachment();
        //attachment2.setPath("/Users/xlizy/Pictures/icon/xlizy2.png");
        //attachment2.setDisposition(EmailAttachment.ATTACHMENT);
        //attachment2.setDescription("Picture of xlizy2");
        //attachment2.setName("xlizy2");
        //email.attach(attachment2);

        email.send();
    }

}
