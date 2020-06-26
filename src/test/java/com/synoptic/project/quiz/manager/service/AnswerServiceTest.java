package com.synoptic.project.quiz.manager.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.synoptic.project.quiz.manager.model.Answer;
import com.synoptic.project.quiz.manager.model.Question;
import com.synoptic.project.quiz.manager.repository.AnswerRepository;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnswerServiceTest {

  @Mock
  AnswerRepository answerRepositoryMock;

  @InjectMocks
  AnswerService answerService;

  @Test
  public void findAnswerByIdTest() {
    Optional<Answer> answer = Optional.of(Answer.builder()
        .id(1)
        .answer("answer")
        .question(Question.builder().id(1).question("Test").build())
        .build());
    when(answerRepositoryMock.findById(1)).thenReturn(answer);
    Optional<Answer> result = answerService.findOptionalAnswerById(1);
    assertEquals(answer, result);
  }

}