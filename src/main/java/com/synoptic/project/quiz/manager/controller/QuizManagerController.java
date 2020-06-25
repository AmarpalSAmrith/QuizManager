package com.synoptic.project.quiz.manager.controller;

import com.synoptic.project.quiz.manager.model.Pagination;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public abstract class QuizManagerController<T, S> {

  public static final Logger LOG = LoggerFactory.getLogger(QuizManagerController.class);

  protected static final String QUIZ = "quiz";
  protected static final String QUESTION = "question";

  //  ====================== URLs ======================
  protected static final String ROOT_USER = "/user";
  protected static final String ROOT_QUIZ = "/" + QUIZ;
  protected static final String ROOT_QUESTION = "/" + QUESTION;
  protected static final String HOME_URL = "/home/";
  protected static final String EDIT_URL = "/edit/";
  protected static final String DELETE_URL = "/delete/";
  protected static final String UPDATE_URL = "/update/";
  protected static final String VIEW_URL = "/view/";
  protected static final String ADD_URL = "/add";
  protected static final String ADD_QUESTION_URL = "add-question/";
  protected static final String ADD_ANSWER_URL = "add-answer/";
  protected static final String DELETE_ANSWER_URL = "delete-answer/";
  protected static final String ADD_QUIZ_NAME_URL = "quiz-name/";
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

  protected static List<String> getFieldsList(List<String> fields, Predicate<String> predicate) {
    return fields.stream()
        .filter(predicate)
        .collect(Collectors.toList());
  }

  public ModelAndView getAllResults(ModelAndView model, String uri, Integer pageNumber,
      Integer pageSize, Page<T> results) {

    Pagination<T> pagination = new Pagination<>(results, pageSize, pageNumber);
    if (!pagination.isPaginationValid()) {
      return new ModelAndView("redirect:" + uri + HOME_URL +
          pagination.getValidUrl());
    }

    model.addObject("pagination", pagination);
    model.addObject("uri", uri + "/home");
    model.addObject("viewUri", uri + VIEW_URL);
    model.addObject("deleteUri", uri + DELETE_URL);
    model.addObject("editUri", uri + EDIT_URL);
    model.addObject("addUri", uri + ADD_URL);
    model.addObject("root", uri);
    return model;
  }

  public ModelAndView editResult(T result, String modelName, String linkAction, List<String> fields,
      String name, String viewUri, List<S> allOptions, String addOptionAction,
      String linkActionTitle) {

    ModelAndView model = new ModelAndView(modelName);
    model.addObject("result", result);
    model.addObject("name", name);
    model.addObject("formType", "edit");
    model.addObject("linkAction", linkAction + UPDATE_URL);
    model.addObject("viewUri", viewUri + VIEW_URL);
    model.addObject("homeUri", linkAction);
    model.addObject("listOfFields", fields);
    model.addObject("allOptions", allOptions);
    model.addObject("addOptionAction", addOptionAction);
    model.addObject("linkActionTitle", linkActionTitle);

    return model;
  }

  public ModelAndView addResult(String modelName, String linkAction, T result,
      String name, String viewUri) {

    ModelAndView model = new ModelAndView(modelName);
    model.addObject("result", result);
    model.addObject("name", name);
    model.addObject("linkAction", linkAction + ADD_URL);
    model.addObject("viewUri", viewUri + VIEW_URL);
    model.addObject("homeUri", linkAction);
    model.addObject("formType", "add");
    return model;
  }

  public ModelAndView viewResult(T result, String modelName, String linkAction,
      List<String> fields, String name, String viewUri) {
    ModelAndView model = new ModelAndView(modelName);
    model.addObject("result", result);
    model.addObject("name", name);
    model.addObject("formType", "view");
    model.addObject("linkAction", linkAction + EDIT_URL);
    model.addObject("homeUri", linkAction);
    model.addObject("listOfFields", fields);
    model.addObject("viewUri", viewUri + VIEW_URL);
    return model;
  }

  public RedirectView submitRedirect(String url) {
    return new RedirectView(url);
  }
}
