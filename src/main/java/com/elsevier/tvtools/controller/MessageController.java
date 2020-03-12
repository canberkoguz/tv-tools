package com.elsevier.tvtools.controller;

import com.elsevier.tvtools.service.MessageService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @CrossOrigin
  @GetMapping(path = "/stream/{tvName}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<String>> streamMessages(@PathVariable String tvName) {
    return messageService.streamMessages(tvName);
  }

}
