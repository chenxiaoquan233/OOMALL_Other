package cn.edu.xmu.oomall.other.util;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sendinblue.*;
import sendinblue.auth.*;
import sibModel.*;
import sibApi.TransactionalEmailsApi;
import java.util.*;

/**
 * @author XQChen
 * @version 创建时间：2020/12/8 上午8:59
 */
public class MailUtil {

    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    public ResponseCode sendEmail(String to, String name, String msg) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        // Configure API key authorization: api-key
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey("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");

        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

        SendSmtpEmailTo sendSmtpEmailTo = new SendSmtpEmailTo();
        sendSmtpEmailTo.setEmail(to);
        sendSmtpEmailTo.setName(name);

        SendSmtpEmailSender sendSmtpEmailSender = new SendSmtpEmailSender();
        sendSmtpEmailSender.setEmail("XQChen@oomall.com");
        sendSmtpEmailSender.setName("XQChen");

        try {
            SendSmtpEmail email = new SendSmtpEmail();

            email.setSender(sendSmtpEmailSender);
            email.setTo(Arrays.asList(sendSmtpEmailTo));
            email.setTextContent(msg);
            email.setSubject("[XMU OOMall] 密码重置通知");

            CreateSmtpEmail result = apiInstance.sendTransacEmail(email);


        } catch (ApiException ex) {
            logger.debug("[Email] failed: "+ ex.getResponseBody());
            return ResponseCode.INTERNAL_SERVER_ERR;
        }

        return ResponseCode.OK;
    }
}
