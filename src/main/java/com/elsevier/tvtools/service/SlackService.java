package com.elsevier.tvtools.service;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import java.util.List;
import java.util.stream.Collectors;
import com.elsevier.tvtools.model.SlackResponse;
import com.elsevier.tvtools.model.TV;
import com.slack.api.model.block.composition.TextObject;
import org.springframework.stereotype.Service;

@Service
public class SlackService {

  private static final String TV_EMOJI = ":tv: ";
  private static final String TV_LIST_MESSAGE = "List of possible Tv names:";

  public SlackResponse getTvList() {
    List<TextObject> fields = TV.NAMES.stream()
        .map(tv -> plainText(pt -> pt.emoji(true).text(TV_EMOJI + tv)))
        .collect(Collectors.toList());

    return SlackResponse.builder()
        .blocks(asBlocks(
            section(section -> section.text(markdownText(TV_LIST_MESSAGE))),
            divider(),
            section(section -> section.fields(fields))
        )).build();
  }

}
