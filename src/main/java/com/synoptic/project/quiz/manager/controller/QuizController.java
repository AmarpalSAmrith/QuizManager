package com.synoptic.project.quiz.manager.controller;

import com.synoptic.project.quiz.manager.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class QuizController {

  private final QuizService quizService;

  public static final String ROOT_FOLDER = "quizzes";

  //  ====================== URLs ======================
  protected static final String ROOT_USER = "/user";
  protected static final String ROOT_QUIZ = "/quiz";

  protected static final String HOME_URL = "/home/";
  protected static final String EDIT_URL = "/edit/";
  protected static final String DELETE_URL = "/delete/";
  protected static final String UPDATE_URL = "/update/";
  protected static final String VIEW_URL = "/view/";
  protected static final String ADD_URL = "/add";

  //  ================== VIEW_NAME =============================
  protected static final String INDEX_VIEW_NAME = "/index";
  protected static final String NEW_VIEW_NAME = "/new";
  protected static final String EDIT_VIEW_NAME = "/edit";

  @Autowired
  public QuizController(QuizService quizService) {
    this.quizService = quizService;
  }

  @GetMapping(ROOT_QUIZ + HOME_URL + "{pageNumber}/{pageSize}")
  public ModelAndView getAllQuizzes(@PathVariable int pageNumber, @PathVariable int pageSize){
    return new ModelAndView(ROOT_FOLDER + INDEX_VIEW_NAME);
  }

}
