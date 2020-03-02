package com.elsevier.tvtools.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import com.elsevier.tvtools.model.Block;
import com.elsevier.tvtools.model.Field;
import com.elsevier.tvtools.model.TV;
import org.springframework.stereotype.Service;

@Service
public class SlackService {

  private static final String BLOCK_TYPE = "section";
  private static final String FIELD_TYPE = "plain_text";
  private static final String TV_EMOJI = ":tv: ";

  public List<Block> getTvBlocks() {
    List<Field> fields = TV.NAMES.stream().map(tv -> Field.builder()
        .text(TV_EMOJI + tv)
        .type(FIELD_TYPE)
        .emoji(true)
        .build())
        .collect(Collectors.toList());
    return Collections.singletonList(Block.builder()
        .type(BLOCK_TYPE)
        .fields(fields)
        .build());
  }

}
