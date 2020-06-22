package com.synoptic.project.quiz.manager.controller;

import com.synoptic.project.quiz.manager.model.Pagination;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public abstract class QuizManagerController<T> {

  public static final Logger LOG = LoggerFactory.getLogger(QuizManagerController.class);

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

  protected static boolean isValidInteger(String number) {
    try {
      int parsedInteger = Integer.parseInt(number);
      if (parsedInteger > 0) {
        return true;
      } else {
        return false;
      }
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public ModelAndView getAllQuizzes(ModelAndView model, String uri, Integer pageNumber,
      Integer pageSize, Page<T> results, List<String> fields) {

    return getAllQuizzes(model, uri, pageNumber, pageSize, results,
        fields.get(0), null, fields);
  }

  public ModelAndView getAllQuizzes(ModelAndView model, String uri, Integer pageNumber,
      Integer pageSize, Page<T> results, String searchBy, String searchValue, List<String> fields) {

    Pagination<T> pagination = new Pagination<>(results, pageSize, pageNumber);
    if (!pagination.isPaginationValid()) {
      return new ModelAndView("redirect:" + uri + "/" +
          pagination.getValidUrl());
    }

    model.addObject("searchBy", searchBy);
    model.addObject("searchValue", searchValue);
    model.addObject("searchByValues", fields);
    model.addObject("searchAction", uri + HOME_URL + pagination.getValidUrl());
    model.addObject("pagination", pagination);
    model.addObject("uri", uri + "/home");
    model.addObject("viewUri", uri + VIEW_URL);
    model.addObject("deleteUri", uri + DELETE_URL);
    model.addObject("editUri", uri + EDIT_URL);
    model.addObject("addUri", uri + ADD_URL);
    model.addObject("root", uri);
//    model.addObject("quizViewUri", quizViewUri);
//    model.addObject("userViewUri", userViewUri);
    return model;
  }

  public ModelAndView editResult(T result, String modelName, String linkAction,
      List<String> fields) {

    ModelAndView model = new ModelAndView(modelName);
    model.addObject("result", result);
//    model.addObject("urlList", "");
    model.addObject("formType", "edit");
    model.addObject("linkAction", linkAction + UPDATE_URL);
    model.addObject("listOfFields", fields);
    return model;
  }

  public ModelAndView addResult(String modelName, String linkAction, T result,
      List<String> fields) {

    ModelAndView model = new ModelAndView(modelName);
    model.addObject("result", result);
//    model.addObject("urlList");
    model.addObject("linkAction", linkAction + ADD_URL);
    model.addObject("formType", "add");
    model.addObject("listOfFields", fields);
    return model;
  }

  public ModelAndView viewResult(T result, String modelName, String linkAction,
      List<String> fields) {
    ModelAndView model = new ModelAndView(modelName);
    model.addObject("result", result);
//    model.addObject("urlList");
    model.addObject("formType", "view");
    model.addObject("linkAction", linkAction + EDIT_URL);
    model.addObject("listOfFields", fields);
    return model;
  }

  public RedirectView submitRedirect(String url) {
    return new RedirectView(url);
  }
}
