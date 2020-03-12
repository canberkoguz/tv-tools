package com.elsevier.tvtools.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slack.api.model.block.LayoutBlock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackResponse {

  @JsonProperty("text")
  private String text;

  @JsonProperty("response_type")
  private String responseType;

  @JsonProperty("blocks")
  private List<LayoutBlock> blocks;


}