package com.synoptic.project.quiz.manager.service;

import com.synoptic.project.quiz.manager.model.Question;
import com.synoptic.project.quiz.manager.model.Quiz;
import com.synoptic.project.quiz.manager.repository.QuizRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

  private final QuizRepository quizRepository;

  @Autowired
  public QuizService(QuizRepository quizRepository) {
    this.quizRepository = quizRepository;
  }

  public Page<Quiz> getAllQuizzesOrderedByName(Pageable pageable) {
    return quizRepository.findAll(pageable);
  }

  public Optional<Quiz> findQuizById(Integer id) {
    return quizRepository.findById(id);
  }

  public Quiz updateQuiz(Quiz quiz) {
    List<Question> quizOptional = findQuizById(quiz.getId())
        .orElseThrow(() -> new IllegalArgumentException("Incorrect ID given"))
        .getQuestions();
    quiz.setQuestions(quiz.getQuestions().stream()
        .filter(i -> i.getId() != null)
        .map(i -> quizOptional.get(quizOptional.indexOf(i)))
        .collect(Collectors.toList()));
    return quizRepository.save(quiz);
  }

  public Quiz createQuiz(Quiz quiz) {
    return quizRepository.save(quiz);
  }

  public void addQuestion(Integer quizId, Question question) {
    Quiz quiz = findQuizById(quizId).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
    quiz.getQuestions().add(question);
    quizRepository.save(quiz);
  }

  public void deleteQuizById(int id) {
    Quiz quiz = findQuizById(id)
        .orElseThrow(() -> new IllegalArgumentException("Incorrect ID given"));
    quiz.setQuestions(new ArrayList<>());
    quizRepository.save(quiz);
//    quizRepository.delete(quiz);
  }


}
