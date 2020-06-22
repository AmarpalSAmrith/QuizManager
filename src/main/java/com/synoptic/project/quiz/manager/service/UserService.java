package com.synoptic.project.quiz.manager.service;

import com.synoptic.project.quiz.manager.model.MyUserDetails;
import com.synoptic.project.quiz.manager.model.User;
import com.synoptic.project.quiz.manager.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Page<User> getAllUsersOrderedByFirstName(Pageable pageable) {
    Page<User> result = userRepository.findAll(pageable);
    return result;
  }

  public Page<User> getAllUsersFilteredById(String id, Pageable pageable) {
    return userRepository.findByIdContaining(id, pageable);
  }

  public Page<User> getAllUsersFilteredByUsername(String username, Pageable pageable) {
    return userRepository.findByUsernameContaining(username, pageable);
  }

  public Page<User> getAllUsersFilteredByFirstName(String firstName, Pageable pageable) {
    return userRepository.findByFirstNameContaining(firstName, pageable);
  }

  public Page<User> getAllUsersFilteredByLastName(String lastName, Pageable pageable) {
    return userRepository.findByLastNameContaining(lastName, pageable);
  }

  public Page<User> getAllUsersFilteredByActive(String active, Pageable pageable) {
    return userRepository.findByActiveContaining(active, pageable);
  }

  public Optional<User> findUserById(int id) {
    return userRepository.findById(id);
  }

  public User updateOrCreateUser(User user) {
    return userRepository.save(user);
  }

  public void deleteUserById(int id) {
    userRepository.deleteById(id);
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(s);
    return new MyUserDetails(user);
  }
}
