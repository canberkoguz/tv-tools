package com.elsevier.tvtools.controller;

import java.nio.file.AccessDeniedException;
import com.elsevier.tvtools.model.SlackResponse;
import com.elsevier.tvtools.model.TV;
import com.elsevier.tvtools.service.MessageService;
import com.elsevier.tvtools.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackController {

  private final MessageService messageService;
  private final SlackService slackService;

  @Value("${SLACK_TOKEN}")
  private String slackToken;

  @PostMapping(value = "/display", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public SlackResponse onReceiveDisplayCommand(@RequestParam("text") String text,
                                               @RequestParam("token") String token) throws AccessDeniedException {
    if (!slackToken.equals(token)) {
      throw new AccessDeniedException("Forbidden!");
    }
    if (StringUtils.isEmpty(text)) {
      return buildResponse("Empty message!");
    }
    String[] messageWords = text.split(" ");
    if (messageWords.length < 1 || !TV.isValid(messageWords[0])) {
      return buildResponse("Invalid TV name! Run /listTvs command");
    }
    if (messageWords.length < 2) {
      return buildResponse("Invalid message!");
    }
    String tvName = messageWords[0];
    String messageText = text.substring(tvName.length()).trim();
    messageService.sendMessage(tvName, messageText);
    return buildResponse("Sent to TV: " + text);
  }

  @PostMapping(value = "/listTvs", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public SlackResponse onReceiveListTvsCommand() {
    return slackService.getTvList();
  }

  private SlackResponse buildResponse(String text) {
    return SlackResponse.builder()
        .text(text)
        .build();
  }

}