package com.synoptic.project.quiz.manager.model;

import com.synoptic.project.quiz.manager.values.PageSize;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class Pagination<T> {

  private Map<Integer, String> pageSizesAndUrls;
  private Page<T> pages;
  private int currentPageSize;
  private int currentPageNumber;
  private boolean isPaginationValid = true;

  public Pagination(
      Page<T> pages,
      int currentPageSize,
      int currentPageNumber) {

    this.pages = pages;

    this.currentPageSize = getValidPageSize(currentPageSize);

    this.currentPageNumber = getValidPageNumber(currentPageNumber);

    this.pageSizesAndUrls = getPageSizesAndUrlsMap();
  }

  public static boolean isNotValidInteger(int index) {
    return index < 1;
  }

  public static String getDefaultURL() {
    return "1/" + PageSize.getFirstPageSize();
  }

  public String getValidUrl() {
    return currentPageNumber + "/" + currentPageSize;
  }

  public Map<Integer, String> getPageSizesAndUrlsMap() {
    return Arrays.stream(PageSize.values())
        .collect(Collectors.toMap(PageSize::getValue,
            i -> getPageNumberForPageSize(i.getValue()) + "/" + i.getValue(),
            (e1, e2) -> e1,
            LinkedHashMap::new));
  }

  public int getValidPageNumber(int pageNumber) {
    if (pageNumber < 1) {
      isPaginationValid = false;
      return 1;
    } else if (pageNumber > pages.getTotalPages()) {
      isPaginationValid = false;
      return pages.getTotalPages();
    }
    return pageNumber;
  }

  public int getValidPageSize(int pageSize) {
    if (!PageSize.isValid(pageSize)) {
      isPaginationValid = false;
      return PageSize.getNearestPageSize(pageSize);
    } else {
      return pageSize;
    }
  }

  public List<T> getPagesList() {
    return pages.toList();
  }

  public List<Integer> getListOfPageNumbers() {
    int totalPages = pages.getTotalPages();
    if (pages.getTotalPages() > 3) {
      if (currentPageNumber == 1) {
        return IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList());
      } else if (currentPageNumber == totalPages) {
        return IntStream.rangeClosed(totalPages - 2, totalPages)
            .boxed()
            .collect(Collectors.toList());
      } else {
        return IntStream.rangeClosed(currentPageNumber - 1, currentPageNumber + 1)
            .boxed()
            .collect(Collectors.toList());
      }
    } else {
      return IntStream.rangeClosed(1, totalPages)
          .boxed()
          .collect(Collectors.toList());
    }
  }

  public boolean isNextAvailable() {
    return currentPageNumber != pages.getTotalPages();
  }

  public boolean isPreviousAvailable() {
    return currentPageNumber != 1;
  }

  public boolean isNotFirstPage() {
    return currentPageNumber > 2;
  }

  public boolean isNotLastPage() {
    return currentPageNumber < pages.getTotalPages() - 1;
  }

  public int getPreviousPageNumber() {
    return currentPageNumber - 1;
  }

  public int getNextPageNumber() {
    return currentPageNumber + 1;
  }

  public String getCurrentRecordsDisplayed() {
    int i = (currentPageNumber - 1) * currentPageSize + 1;
    return String.valueOf(i) +
        " - " +
        (Math.min(i + currentPageSize - 1, i + pages.getContent().size() - 1));
  }

  public int getPageNumberForPageSize(int pageSize) {
    int maxPageNumber = (int) Math.ceil((double) pages.getTotalElements() / pageSize);
    if (currentPageNumber > maxPageNumber) {
      return maxPageNumber;
    } else {
      return currentPageNumber;
    }
  }

}
