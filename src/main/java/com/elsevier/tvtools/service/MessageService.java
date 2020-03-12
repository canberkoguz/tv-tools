package com.elsevier.tvtools.service;

import com.elsevier.tvtools.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;

  public void sendMessage(String tvName, String text) {
    messageRepository.saveMessage(tvName, text);
  }

  public Flux<String> streamMessages(String tvName) {
    return Flux.create(fluxSink -> {
      while (true) {
        try {
          String message = messageRepository.getMessage(tvName);
          fluxSink.next(message);
        } catch (InterruptedException e) {
          ReflectionUtils.rethrowRuntimeException(e);
        }
      }
    });
  }

}
