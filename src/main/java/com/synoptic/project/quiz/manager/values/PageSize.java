package com.synoptic.project.quiz.manager.values;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.stream.Collectors;


public enum PageSize {

  SIZE_5(0, 5),

  SIZE_10(1, 10),

  SIZE_25(2, 25),

  SIZE_50(3, 50),

  SIZE_100(4, 100),

  SIZE_200(5, 200);

  private int id;
  private int value;

  PageSize(int id, int value) {
    this.value = value;
    this.id = id;
  }

  public static boolean isValid(int pageSize) {
    return Arrays.stream(PageSize.values())
        .map(PageSize::getValue)
        .collect(Collectors.toList())
        .contains(pageSize);
  }

  public static int getFirstPageSize() {
    return PageSize.values()[0].value;
  }

  public static int getValidatedPageSize(int pageSize) {
    return isValid(pageSize) ? pageSize : getNearestPageSize(pageSize);
  }

  public static int getNearestPageSize(int pageSize) {
    return
        Collections.min(Arrays.stream(PageSize.values())
            .collect(Collectors.toMap(PageSize::getValue,
                i -> Math.abs(i.getValue() - pageSize))).entrySet(), Entry.comparingByValue())
            .getKey();
  }

  public int getValue() {
    return value;
  }

  public int getId() {
    return id;
  }
}
