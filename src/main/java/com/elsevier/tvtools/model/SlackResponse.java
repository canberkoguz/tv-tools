package com.elsevier.tvtools.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class SlackResponse {

  @JsonProperty("text")
  private String text;

  @JsonProperty("response_type")
  private String responseType;

  @JsonProperty("blocks")
  private List<Block> blocks;


  public SlackResponse(String text) {
    this.text = text;
  }

  public SlackResponse(List<Block> blocks) {
    this.blocks = blocks;
  }

}