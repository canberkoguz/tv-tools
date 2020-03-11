package com.elsevier.tvtools.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.PostConstruct;
import com.elsevier.tvtools.model.TV;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {

  private final Map<String, BlockingQueue<String>> queueMap = new HashMap<>();

  @PostConstruct
  public void initQueues() {
    TV.NAMES.forEach(tvName -> queueMap.put(tvName, new LinkedBlockingQueue<>()));
  }

  public void saveMessage(String tvName, String text) {
    BlockingQueue<String> messageQueue = queueMap.get(tvName);
    messageQueue.add(text);
  }

  public String getMessage(String tvName) throws InterruptedException {
    BlockingQueue<String> messageQueue = queueMap.get(tvName);
    return messageQueue.take();
  }

}
