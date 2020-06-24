package com.synoptic.project.quiz.manager.controller;

import com.synoptic.project.quiz.manager.model.Pagination;
import com.synoptic.project.quiz.manager.model.Question;
import com.synoptic.project.quiz.manager.model.Quiz;
import com.synoptic.project.quiz.manager.service.QuestionService;
import com.synoptic.project.quiz.manager.service.QuizService;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class QuizController extends QuizManagerController<Quiz, Question> {

  public static final String ROOT_FOLDER = "quizzes";

  private final QuizService quizService;
  private final QuestionService questionService;

  @Autowired
  public QuizController(QuizService quizService,
      QuestionService questionService) {
    this.quizService = quizService;
    this.questionService = questionService;
  }

  private static List<String> getFields() {
    return Arrays.stream(Quiz.class.getDeclaredFields())
        .map(Field::getName)
        .collect(Collectors.toList());
  }

  @GetMapping(ROOT_QUIZ)
  public RedirectView getQuizHomePage() {
    return super.submitRedirect(ROOT_QUIZ + HOME_URL + Pagination.getDefaultURL());
  }

  @GetMapping(ROOT_QUIZ + HOME_URL + "{pageNumber}/{pageSize}")
  public ModelAndView getAllQuizzes(@PathVariable Integer pageNumber,
      @PathVariable Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Direction.ASC, "name");

    Page<Quiz> results = quizService.getAllQuizzesOrderedByName(pageable);
    ModelAndView model = new ModelAndView(ROOT_FOLDER + INDEX_VIEW_NAME);

    return super.getAllQuizzes(model, ROOT_QUIZ, pageNumber, pageSize, results);
  }

  @GetMapping(ROOT_QUIZ + EDIT_URL + "{id}")
  public ModelAndView editQuiz(@PathVariable Integer id) {
    Quiz quiz = quizService.findQuizById(id)
        .orElseThrow(() -> new IllegalArgumentException("No Such Quiz Exists With ID: " + id));
    return super.editResult(quiz,
        ROOT_FOLDER + EDIT_VIEW_NAME,
        ROOT_QUIZ, Collections.singletonList("name"), QUIZ, ROOT_QUESTION,
        questionService.getAllQuestions(), ROOT_QUIZ + ADD_QUESTION_URL,
        ROOT_QUIZ + UPDATE_URL + ADD_QUIZ_NAME_URL);
  }

  @GetMapping(ROOT_QUIZ + ADD_QUESTION_URL + "{quizId}")
  public RedirectView addQuestionToQuiz(@PathVariable Integer quizId,
      @ModelAttribute(value = "addOption") Integer questionId) {
    //FIXME: should this be a post?
    quizService.addQuestion(quizId, questionService.findQuestionById(questionId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid ID")));
    return new RedirectView(ROOT_QUIZ + EDIT_URL + quizId);
  }

  @GetMapping(ROOT_QUIZ + ADD_URL)
  public ModelAndView addQuiz(HttpSession session) {
    return super.addResult(ROOT_FOLDER + NEW_VIEW_NAME,
        ROOT_QUIZ, new Quiz(), getFields(), QUIZ, ROOT_QUESTION);
  }

  @GetMapping(ROOT_QUIZ + VIEW_URL + "{id}")
  public ModelAndView viewQuiz(@PathVariable Integer id) {
    return super
        .viewResult(quizService.findQuizById(id).get(), ROOT_FOLDER + EDIT_VIEW_NAME,
            ROOT_QUIZ, Collections.singletonList("name"), QUIZ, ROOT_QUESTION);
  }

  @PostMapping(ROOT_QUIZ + UPDATE_URL + "{id}")
  public RedirectView updateQuizQuestions(@ModelAttribute Quiz quiz, @PathVariable int id) {
    quizService.updateQuizQuestions(quiz);
    return super.submitRedirect(ROOT_QUIZ + EDIT_URL + id);
  }

  @PostMapping(ROOT_QUIZ + UPDATE_URL + ADD_QUIZ_NAME_URL + "{id}")
  public RedirectView updateQuizName(@ModelAttribute Quiz quiz, @PathVariable Integer id) {
    quizService.updateQuizName(quiz);
    return submitRedirect(ROOT_QUIZ + EDIT_URL + id);
  }

  @PostMapping(ROOT_QUIZ + ADD_URL)
  public RedirectView createQuiz(@ModelAttribute Quiz quiz, HttpSession session) {
    session.removeAttribute("quiz");
    Quiz newQuiz = quizService.createQuiz(quiz);
    return submitRedirect(ROOT_QUIZ + EDIT_URL + newQuiz.getId());
  }

  @PostMapping(ROOT_QUIZ + DELETE_URL + "{id}")
  public RedirectView deleteQuiz(@PathVariable int id) {
    //FIXME: not working at all ask Ruben
    quizService.deleteQuizById(id);
    return submitRedirect(ROOT_QUIZ);
  }
}