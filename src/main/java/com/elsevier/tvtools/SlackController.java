package com.elsevier.tvtools;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackController {

  private static final String SQS_URL = "https://sqs.eu-west-1.amazonaws.com/804335263071/hackaton-tv-tool";

  private final AmazonSQS awsSqsClient;
  private static final String[] TV_NAMES = new String[] {"tv-fi", "tv-fs", "tv-gh", "tv-el"};
  private static final Set<String> TV_SET = new HashSet<>(Arrays.asList(TV_NAMES));
  private static final String BLOCK_TYPE = "section";
  private static final String FIELD_TYPE = "plain_text";

  @RequestMapping(value = "/display",
                  method = RequestMethod.POST,
                  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public SlackResponse onReceiveDisplayCommand(@RequestParam("text") String text) {
    if (StringUtils.isEmpty(text)) {
      return new SlackResponse("Empty message!");
    }
    String[] messageWords = text.split(" ");
    if (messageWords.length < 1 || !TV_SET.contains(messageWords[0])) {
      return new SlackResponse("Invalid TV name! Run /listTvs command");
    }
    SendMessageRequest sendMessageStandardQueue = new SendMessageRequest()
        .withQueueUrl(SQS_URL)
        .withMessageBody(text)
        .withDelaySeconds(0);
    awsSqsClient.sendMessage(sendMessageStandardQueue);
    return new SlackResponse("Sent to TV: " + text);
  }

  @RequestMapping(value = "/listTvs",
                  method = RequestMethod.POST,
                  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public SlackResponse onReceiveListTvsCommand() {
    return new SlackResponse(getTvBlocks());
  }

  private List<Block> getTvBlocks() {
    List<Field> fields = TV_SET.stream().map(tv -> Field.builder()
        .text(tv)
        .type(FIELD_TYPE)
        .build())
        .collect(Collectors.toList());
    return Collections.singletonList(Block.builder()
        .type(BLOCK_TYPE)
        .fields(fields)
        .build());
  }

}