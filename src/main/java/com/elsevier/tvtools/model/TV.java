package com.elsevier.tvtools.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TV {

  EL("tv-el"),
  GH("tv-gh"),
  FI("tv-fi"),
  FS("tv-fs");

  public static final Set<String> NAMES = new HashSet<>(Arrays.stream(values())
      .map(TV::getName)
      .collect(Collectors.toList()));

  @Getter
  private final String name;

  public static boolean isValid(String name) {
    return NAMES.contains(name);
  }

}
