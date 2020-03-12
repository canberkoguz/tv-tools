package com.elsevier.tvtools.service;

import java.time.Duration;
import com.elsevier.tvtools.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MessageService {

  private static final ServerSentEvent<String> KEEP_ALIVE_EVENT = ServerSentEvent.<String>builder()
      .comment("keep alive")
      .build();

  private final MessageRepository messageRepository;

  public void sendMessage(String tvName, String text) {
    messageRepository.saveMessage(tvName, text);
  }

  public Flux<ServerSentEvent<String>> streamMessages(String tvName) {
    return Flux.interval(Duration.ofSeconds(1L))
        .map(seq -> getMessage(tvName));
  }

  private ServerSentEvent<String> getMessage(String tvName) {
    String message = messageRepository.getMessage(tvName);
    if (StringUtils.isEmpty(message)) {
      return KEEP_ALIVE_EVENT;
    } else {
      return ServerSentEvent.builder(message).build();
    }
  }

}
