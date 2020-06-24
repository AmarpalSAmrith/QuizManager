package com.synoptic.project.quiz.manager.repository;

import com.synoptic.project.quiz.manager.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Integer> {



}
