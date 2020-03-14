package com.elsevier.tvtools.controller;

import com.elsevier.tvtools.model.TV;
import com.elsevier.tvtools.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @CrossOrigin
  @GetMapping("/stream/{tvName}")
  public ResponseEntity<SseEmitter> streamMessages(@PathVariable String tvName) {
    if (!TV.isValid(tvName)) {
      return ResponseEntity.badRequest().build();
    }
    final SseEmitter emitter = new SseEmitter();
    messageService.addEmitter(tvName, emitter);
    messageService.keepALive();
    emitter.onCompletion(() -> messageService.removeEmitter(tvName, emitter));
    emitter.onTimeout(() -> messageService.removeEmitter(tvName, emitter));
    return ResponseEntity.ok(emitter);
  }

}
