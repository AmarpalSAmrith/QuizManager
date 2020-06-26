package com.synoptic.project.quiz.manager.service;

import com.synoptic.project.quiz.manager.model.Question;
import com.synoptic.project.quiz.manager.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

  QuestionRepository questionRepository;
  AnswerService answerService;
  QuizService quizService;

  @Autowired
  public QuestionService(
      QuestionRepository questionRepository, AnswerService answerService, QuizService quizService) {
    this.questionRepository = questionRepository;
    this.answerService = answerService;
    this.quizService = quizService;
  }

  public Page<Question> getAllQuestionsOrderedByQuestion(Pageable pageable) {
    return questionRepository.findAll(pageable);
  }

  public List<Question> getAllQuestions() {
    return questionRepository.findAll(Sort.by(Direction.ASC, "question"));
  }

  public Question findQuestionById(Integer id) {
    return findOptionalQuestionById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid Question ID: " + id));
  }


  public Optional<Question> findOptionalQuestionById(Integer id) {
    return questionRepository.findById(id);
  }

  public Question updateOrCreateQuestion(Question question) {

    return questionRepository.save(question);
  }

  public void deleteQuestionById(int id) {
    Question question = findQuestionById(id);
    question.getQuizzes().forEach(quiz -> {
          quiz.setQuestions(quiz.getQuestions().stream()
              .filter(question1 -> !question1.getId().equals(id))
              .collect(Collectors.toList()));
        }
    );
    questionRepository.save(question);
    question.setQuizzes(new ArrayList<>());
    question.getAnswers().forEach(answer -> answerService.deleteAnswer(answer));
    question.setAnswers(null);
    updateOrCreateQuestion(question);
    questionRepository.deleteById(id);
  }
}