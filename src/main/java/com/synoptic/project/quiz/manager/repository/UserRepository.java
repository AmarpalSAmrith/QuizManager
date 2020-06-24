package com.synoptic.project.quiz.manager.repository;

import com.synoptic.project.quiz.manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findByUsername(String username);

}
