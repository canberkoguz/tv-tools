package com.elsevier.tvtools.controller;

import com.elsevier.tvtools.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @GetMapping(value = "/receive/{tvName}")
  public String receiveMessage(@PathVariable String tvName) {
    return messageService.receiveMessage(tvName);
  }

}
