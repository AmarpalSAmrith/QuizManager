package com.synoptic.project.quiz.manager.repository;

import com.synoptic.project.quiz.manager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

//  @Query(value = "SELECT * FROM quiz_manager.user WHERE CAST(id as CHAR) LIKE %?1%", nativeQuery = true)
//  Page<User> findByIdContaining(String searchValue, Pageable pageable);

  Page<User> findByFirstNameContaining(String searchValue, Pageable pageable);

  Page<User> findByLastNameContaining(String searchValue, Pageable pageable);

  Page<User> findByActiveContaining(String searchValue, Pageable pageable);

  Page<User> findByUsernameContaining(String searchValue, Pageable pageable);

  User findByUsername(String username);

}
