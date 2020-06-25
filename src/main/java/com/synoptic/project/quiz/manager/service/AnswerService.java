package com.synoptic.project.quiz.manager.service;

import com.synoptic.project.quiz.manager.model.Answer;
import com.synoptic.project.quiz.manager.model.Question;
import com.synoptic.project.quiz.manager.repository.AnswerRepository;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

  AnswerRepository answerRepository;

  public AnswerService(AnswerRepository answerRepository) {
    this.answerRepository = answerRepository;
  }

  public Answer updateOrCreateAnswer(Answer answer) {
    return answerRepository.save(answer);
  }

  public Question updateAnswersOnQuestion(Question questionRef) {
    questionRef.getAnswers().forEach(this::updateOrCreateAnswer);
    return questionRef;
  }
}
