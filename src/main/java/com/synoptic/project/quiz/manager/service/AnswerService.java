package com.synoptic.project.quiz.manager.service;

import com.synoptic.project.quiz.manager.model.Answer;
import com.synoptic.project.quiz.manager.model.Question;
import com.synoptic.project.quiz.manager.repository.AnswerRepository;
import java.util.Optional;
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

  public Answer findAnswerById(Integer id) {
    return findOptionalAnswerById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid Answer ID: " + id));
  }


  public Optional<Answer> findOptionalAnswerById(Integer id) {
    return answerRepository.findById(id);
  }

  public void deleteAnswerById(Integer id) {
    answerRepository.deleteById(id);
  }

  public void deleteAnswer(Answer answer) {
    answer.setQuestion(null);
    deleteAnswerById(answer.getId());
  }

}
