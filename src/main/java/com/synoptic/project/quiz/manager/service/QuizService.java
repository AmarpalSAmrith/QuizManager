package com.synoptic.project.quiz.manager.service;

import com.synoptic.project.quiz.manager.model.Quiz;
import com.synoptic.project.quiz.manager.repository.QuizRepository;
import java.util.Optional;
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

  public Page<Quiz> getAllQuizzesOrderedByQuestions(Pageable pageable) {
    Page<Quiz> result = quizRepository.findAll(pageable);
    return result;
  }

  public Page<Quiz> getAllQuizzesFilteredById(String id, Pageable pageable) {
    return quizRepository.findByIdContaining(id, pageable);
  }

  public Page<Quiz> getAllQuizzesFilteredByName(String name, Pageable pageable) {
    return quizRepository.findByNameContaining(name, pageable);
  }

  public Optional<Quiz> findQuizById(Integer id){
    return quizRepository.findById(id);
  }

  public Quiz updateOrCreateQuiz(Quiz quiz) {
    return quizRepository.save(quiz);
  }

  public void deleteQuizById(int id){
    quizRepository.deleteById(id);
  }
  
  
}
