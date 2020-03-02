package com.elsevier.tvtools.controller;

import com.elsevier.tvtools.model.SlackResponse;
import com.elsevier.tvtools.model.TV;
import com.elsevier.tvtools.service.MessageService;
import com.elsevier.tvtools.service.SlackService;
import lombok.RequiredArgsConstructor;
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

  @PostMapping(value = "/display", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public SlackResponse onReceiveDisplayCommand(@RequestParam("text") String text) {
    if (StringUtils.isEmpty(text)) {
      return new SlackResponse("Empty message!");
    }
    String[] messageWords = text.split(" ");
    if (messageWords.length < 1 || !TV.isValid(messageWords[0])) {
      return new SlackResponse("Invalid TV name! Run /listTvs command");
    }
    if (messageWords.length < 2) {
      return new SlackResponse("Invalid message!");
    }
    String tvName = messageWords[0];
    String messageText = text.substring(tvName.length()).trim();
    messageService.sendMessage(tvName, messageText);
    return new SlackResponse("Sent to TV: " + text);
  }

  @PostMapping(value = "/listTvs", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public SlackResponse onReceiveListTvsCommand() {
    return new SlackResponse(slackService.getTvBlocks());
  }

}