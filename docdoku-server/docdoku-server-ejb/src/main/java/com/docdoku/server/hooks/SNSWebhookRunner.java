package com.docdoku.server.hooks;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.docdoku.core.hooks.SNSWebhookApp;
import com.docdoku.core.hooks.Webhook;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SNSWebhookRunner implements WebhookRunner {

    private static final Logger LOGGER = Logger.getLogger(SNSWebhookRunner.class.getName());

    public SNSWebhookRunner() {
    }

    @Override
    public void run(Webhook webhook, String login, String email, String name, String subject, String content) {

        SNSWebhookApp webhookApp = (SNSWebhookApp) webhook.getWebhookApp();
        String topicArn = webhookApp.getTopicArn();
        String awsAccount = webhookApp.getAwsAccount();
        String awsSecret = webhookApp.getAwsSecret();
        String region = webhookApp.getRegion();
        AmazonSNSClient snsClient = new AmazonSNSClient(new BasicAWSCredentials(awsAccount, awsSecret));
        snsClient.setRegion(Region.getRegion(Regions.fromName(region)));

        try {
            PublishRequest publishReq = new PublishRequest()
                    .withTopicArn(topicArn)
                    .withMessage(getMessage(login, email, name, subject, content));
            snsClient.publish(publishReq);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot send notification to SNS service", e);
        } finally {
            LOGGER.log(Level.INFO, "Webhook runner terminated");
        }
    }

    private String getMessage(String login, String email, String name, String subject, String content) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("login", login);
        objectBuilder.add("email", email);
        objectBuilder.add("name", name);
        objectBuilder.add("subject", subject);
        objectBuilder.add("content", content);
        return objectBuilder.build().toString();
    }

}
