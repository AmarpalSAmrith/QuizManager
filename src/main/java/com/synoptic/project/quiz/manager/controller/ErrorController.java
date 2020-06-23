package com.synoptic.project.quiz.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

  @Autowired
  public ErrorController() {
  }

  @GetMapping("/access-denied")
  public ModelAndView accessDeniedPage(){
    return new ModelAndView("errors/access-denied");
  }

  @GetMapping("/error")
  public ModelAndView errorPage(){
    return new ModelAndView("errors/access-denied");
  }
}
