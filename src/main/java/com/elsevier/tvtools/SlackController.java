package com.elsevier.tvtools;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SlackController {

  private static final String SQS_URL = "https://sqs.us-east-1.amazonaws.com/804335263071/hackaton-tv-tool";
  private static final String DISPLAY_COMMAND = "display";

  private final AmazonSQS awsSqsClient;

  @RequestMapping(value = "/slack/display",
                  method = RequestMethod.POST,
                  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public SlackResponse onReceiveDisplayCommand(@RequestParam("team_id") String teamId,
                                               @RequestParam("team_domain") String teamDomain,
                                               @RequestParam("channel_id") String channelId,
                                               @RequestParam("channel_name") String channelName,
                                               @RequestParam("user_id") String userId,
                                               @RequestParam("user_name") String userName,
                                               @RequestParam("text") String text,
                                               @RequestParam("response_url") String responseUrl) {

    SendMessageRequest sendMessageStandardQueue = new SendMessageRequest()
        .withQueueUrl(SQS_URL)
        .withMessageBody(text)
        .withDelaySeconds(0);
    awsSqsClient.sendMessage(sendMessageStandardQueue);
    return new SlackResponse("Sent to SQS: " + text);
  }

}