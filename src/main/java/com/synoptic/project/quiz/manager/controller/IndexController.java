package com.synoptic.project.quiz.manager.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

  private static final String LANDING_PAGE_VIEW = "landing-page";

  @GetMapping("/")
  public ModelAndView showHomePage(HttpServletRequest request) {
    return new ModelAndView(LANDING_PAGE_VIEW);
  }

}
