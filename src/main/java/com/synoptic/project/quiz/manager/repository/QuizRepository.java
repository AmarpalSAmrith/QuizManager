package com.synoptic.project.quiz.manager.repository;

import com.synoptic.project.quiz.manager.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {

  @Query(value = "SELECT * FROM quiz_manager.quiz WHERE CAST(id as CHAR) LIKE %?1%", nativeQuery = true)
  Page<Quiz> findByIdContaining(String id, Pageable pageable);

  Page<Quiz> findByNameContaining(String name, Pageable pageable);

}
