package com.synoptic.project.quiz.manager.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.synoptic.project.quiz.manager.model.Pagination;
import com.synoptic.project.quiz.manager.values.PageSize;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public class PaginationTest {

  public static List<String> getSampleList(int maxSize) {
    return IntStream.range(0, (int) Math.ceil((double) maxSize / 26))
        .mapToObj(i -> Arrays.stream("abcdefghijklmnopqrstuvwxyz".split("")).toArray(String[]::new))
        .flatMap(Stream::of).limit(maxSize)
        .collect(Collectors.toList());
  }

  @Test
  void getNearestPageSizePassTest() {
    assertEquals(50, PageSize.getNearestPageSize(74));
  }

  @Test
  void getNearestPageSizeFailTest() {
    assertNotEquals("100", PageSize.getNearestPageSize(151));
  }

  @Test
  void getValidUrlTest() {
    int pageSize = PageSize.SIZE_5.getValue();
    int totalElements = 991;
    int pageNumber = 205;
    int correctPagNumber = 200;

    Pageable pageable = PageRequest.of(
        1,
        pageSize,
        Direction.ASC,
        "title"
    );

    Pagination pagination = new Pagination(
        new PageImpl(
            getSampleList(totalElements),
            PageRequest.of(1, pageSize), 0),
        pageSize,
        pageNumber);

    String expected = correctPagNumber + "/" + pageSize;
    String actual =
        pagination.getValidUrl();

    assertEquals(expected, actual);
  }
}
