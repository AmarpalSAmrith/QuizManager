package com.synoptic.project.quiz.manager.service;

import com.synoptic.project.quiz.manager.model.Question;
import com.synoptic.project.quiz.manager.repository.QuestionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

  QuestionRepository questionRepository;

  @Autowired
  public QuestionService(
      QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  public Page<Question> getAllQuestionsOrderedByQuestion(Pageable pageable) {
    return questionRepository.findAll(pageable);
  }

  public List<Question> getAllQuestions() {
    return questionRepository.findAll(Sort.by(Direction.ASC, "question"));
  }

  public Question findQuestionById(Integer id) {
    return questionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid Question ID: " + id));
  }

  public Question updateOrCreateQuestion(Question question) {

    return questionRepository.save(question);
  }

  public Question getOne(Integer id)  {
    return questionRepository.getOne(id);
  }

  public void deleteQuestionById(int id) {
    questionRepository.deleteById(id);
  }


}
