package com.synoptic.project.quiz.manager.service;

  import static org.junit.Assert.assertEquals;
  import static org.mockito.Mockito.when;

  import com.synoptic.project.quiz.manager.model.Question;
  import com.synoptic.project.quiz.manager.repository.QuestionRepository;
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
public class QuestionServiceTest {

  @Mock
  QuestionRepository questionRepositoryMock;

  @InjectMocks
  QuestionService questionService;

  private static List<Question> getQuestionList(int maxSize) {

    String[] alphabetList = IntStream.range(0, (int) Math.ceil((double) maxSize / 26))
        .mapToObj(i -> Arrays.stream("abcdefghijklmnopqrstuvwxyz".split("")).toArray(String[]::new))
        .flatMap(Stream::of)
        .toArray(String[]::new);

    return IntStream.range(0, maxSize)
        .mapToObj(i -> Question.builder()
            .id(i)
            .question(alphabetList[i])
            .build())
        .collect(Collectors.toList());
  }

  @Test
  public void getAllQuestionsOrderedByQuestionTest() {
    Pageable pageable = PageRequest.of(1, PageSize.SIZE_5.getValue(), Direction.ASC, "question");

    List<Question> quizList = getQuestionList(50);

    Page<Question> quizPage = new PageImpl<>(quizList, pageable, 50);

    when(questionRepositoryMock.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(quizPage);

    quizList.sort(Comparator.comparing(Question::getQuestion));
    Page<Question> result = questionService.getAllQuestionsOrderedByQuestion(pageable);
    assertEquals(quizPage, result);
  }

  @Test
  public void findQuestionByIdTest() {
    Optional<Question> question = Optional.of(Question.builder()
        .id(1)
        .question("question")
        .answers(new ArrayList<>())
        .quizzes(new ArrayList<>())
        .build());
    when(questionRepositoryMock.findById(1)).thenReturn(question);
    Optional<Question> result = questionService.findOptionalQuestionById(1);
    assertEquals(question, result);
  }


}