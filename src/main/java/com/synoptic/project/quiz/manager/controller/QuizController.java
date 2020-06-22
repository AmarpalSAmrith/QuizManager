package com.synoptic.project.quiz.manager.controller;

import static com.synoptic.project.quiz.manager.model.Pagination.getDefaultURL;

import com.synoptic.project.quiz.manager.model.Quiz;
import com.synoptic.project.quiz.manager.service.QuizService;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class QuizController extends QuizManagerController<Quiz> {

  public static final String ROOT_FOLDER = "quizzes";

  private final QuizService quizService;

  @Autowired
  public QuizController(QuizService quizService) {
    this.quizService = quizService;
  }

  private static List<String> getFields() {
    return Arrays.stream(Quiz.class.getDeclaredFields())
        .map(Field::getName)
        .collect(Collectors.toList());
  }

  @GetMapping(ROOT_QUIZ + HOME_URL + "{pageNumber}/{pageSize}")
  public ModelAndView getAllQuizzes(@PathVariable Integer pageNumber,
      @PathVariable Integer pageSize, @RequestParam(name = "searchBy") Optional<String> searchBy,
      @RequestParam(name = "searchValue") Optional<String> searchValue) {

    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Direction.ASC, "name");

    Page<Quiz> results;
    ModelAndView model = new ModelAndView(ROOT_FOLDER + INDEX_VIEW_NAME);
    List<String> fields = getFields();

    if (!searchBy.isPresent() && !searchValue.isPresent()) {
      results = quizService.getAllQuizzesOrderedByName(pageable);
      return super.getAllQuizzes(model, ROOT_QUIZ, pageNumber, pageSize, results, fields);
    }

    if ("".equals(searchValue.get())) {
      return new ModelAndView("redirect:" + ROOT_QUIZ + HOME_URL + pageNumber + "/" + pageSize);
    }

    results = getResult(searchBy.get(), searchValue.get(), pageable);

    return super.getAllQuizzes(model, ROOT_QUIZ, pageNumber,
        pageSize, results, searchBy.get(), searchValue.get(), fields);
  }

  private Page<Quiz> getResult(String searchBy, String searchValue, Pageable pageable)
      throws IllegalArgumentException {

    switch (searchBy) {
      case "id":
        if (!isValidInteger(searchValue)) {
          throw new IllegalArgumentException("Invalid Integer");
        }
        return quizService.getAllQuizzesFilteredById(searchValue, pageable);
      case "name":
        return quizService.getAllQuizzesFilteredByName(searchValue, pageable);
    }
    return Page.empty();
  }

  @GetMapping(ROOT_QUIZ + EDIT_URL + "{id}")
  public ModelAndView editQuiz(Integer id) {
    return super.editResult(quizService.findQuizById(id).get(),
        ROOT_FOLDER + EDIT_VIEW_NAME,
        ROOT_QUIZ + UPDATE_URL, getFields());
  }

  @GetMapping(ROOT_QUIZ + ADD_URL)
  public ModelAndView addQuiz() {
    return super.addResult(ROOT_FOLDER + NEW_VIEW_NAME,
        ROOT_QUIZ + ADD_URL, new Quiz(), getFields());
  }

  @GetMapping(ROOT_QUIZ + VIEW_URL + "{id}")
  public ModelAndView viewQuiz(@PathVariable Integer id) {
    return super.viewResult(quizService.findQuizById(id).get(),
        ROOT_FOLDER + EDIT_VIEW_NAME, ROOT_QUIZ, getFields());
  }

  @PostMapping(ROOT_QUIZ + UPDATE_URL + "{id}")
  public RedirectView updateQuiz(@Validated @ModelAttribute Quiz quiz, @PathVariable int id) {
    quizService.updateOrCreateQuiz(quiz);
    return super.submitRedirect(ROOT_QUIZ + VIEW_URL + id);
  }

  @PostMapping(ROOT_QUIZ + ADD_URL)
  public RedirectView createQuiz(@ModelAttribute Quiz quiz) {
    Quiz newQuiz = quizService.updateOrCreateQuiz(quiz);
    return submitRedirect(ROOT_QUIZ + VIEW_URL + newQuiz.getId());
  }

  @PostMapping(ROOT_QUIZ + DELETE_URL + "{id}")
  public RedirectView deleteQuiz(@PathVariable int id) {
    quizService.deleteQuizById(id);
    return submitRedirect(ROOT_QUIZ + HOME_URL + getDefaultURL());
  }

}
