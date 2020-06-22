package com.synoptic.project.quiz.manager.repository;

import com.synoptic.project.quiz.manager.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

  Page<Quiz> findByIdContaining(String id, Pageable pageable);

  Page<Quiz> findByNameContaining(String name, Pageable pageable);

}
