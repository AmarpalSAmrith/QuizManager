package com.synoptic.project.quiz.manager.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.synoptic.project.quiz.manager.model.Quiz;
import com.synoptic.project.quiz.manager.repository.QuizRepository;
import com.synoptic.project.quiz.manager.values.PageSize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceTest {

  @Mock
  QuizRepository quizRepositoryMock;

  @InjectMocks
  QuizService quizService;

  @Test
  public void getAllQuizzesOrderedByNameTest() {
    Pageable pageable = PageRequest.of(1, PageSize.SIZE_5.getValue(), Direction.ASC, "name");

    List<Quiz> quizList = getQuizList(50);

    Page<Quiz> quizPage = new PageImpl<>(quizList, pageable, 50);

    when(quizRepositoryMock.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(quizPage);

    quizList.sort(Comparator.comparing(Quiz::getName));
    Page<Quiz> result = quizService.getAllQuizzesOrderedByName(pageable);
    assertEquals(quizPage, result);
  }

  private static List<Quiz> getQuizList(int maxSize) {

    String[] alphabetList = IntStream.range(0, (int) Math.ceil((double) maxSize / 26))
        .mapToObj(i -> Arrays.stream("abcdefghijklmnopqrstuvwxyz".split("")).toArray(String[]::new))
        .flatMap(Stream::of)
        .toArray(String[]::new);

    return IntStream.range(0, maxSize)
        .mapToObj(i -> Quiz.builder()
            .id(i)
            .name(alphabetList[i])
            .build())
        .collect(Collectors.toList());
  }

  @Test
  public void findQuizByIdTest() {
    Optional<Quiz> quiz = Optional.of(Quiz.builder()
        .id(1)
        .name("quiz")
        .questions(new ArrayList<>())
        .build());
    when(quizRepositoryMock.findById(1)).thenReturn(quiz);
    Optional<Quiz> result = quizService.findQuizById(1);
    assertEquals(quiz,result);
  }

}