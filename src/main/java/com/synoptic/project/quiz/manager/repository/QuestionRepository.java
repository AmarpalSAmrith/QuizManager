package com.synoptic.project.quiz.manager.repository;

import com.synoptic.project.quiz.manager.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

}
