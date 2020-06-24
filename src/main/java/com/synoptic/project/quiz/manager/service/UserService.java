package com.synoptic.project.quiz.manager.service;

import com.synoptic.project.quiz.manager.controller.QuizManagerController;
import com.synoptic.project.quiz.manager.model.Authority;
import com.synoptic.project.quiz.manager.model.MyUserDetails;
import com.synoptic.project.quiz.manager.model.User;
import com.synoptic.project.quiz.manager.repository.UserRepository;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  public static final Logger LOG = LoggerFactory.getLogger(QuizManagerController.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(s);
    LOG.info(user.getUsername() +
        " has: " +
        user.getAuthorities()
            .stream()
            .map(Authority::getAuthority)
            .collect(Collectors.joining(", ")) +
        " Authorities");
    return new MyUserDetails(user);
  }
}
