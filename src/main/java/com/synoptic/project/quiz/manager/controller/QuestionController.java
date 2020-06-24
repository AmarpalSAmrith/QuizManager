package com.synoptic.project.quiz.manager.controller;

import com.synoptic.project.quiz.manager.model.Pagination;
import com.synoptic.project.quiz.manager.model.Question;
import com.synoptic.project.quiz.manager.model.Quiz;
import com.synoptic.project.quiz.manager.service.QuestionService;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class QuestionController extends QuizManagerController<Question, Quiz> {

  public static final String ROOT_FOLDER = "questions";
  private final QuestionService questionService;

  @Autowired
  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @GetMapping(ROOT_QUESTION)
  public RedirectView getQuestionHomePage() {
    return super.submitRedirect(ROOT_QUESTION + HOME_URL + Pagination.getDefaultURL());
  }

  @GetMapping(ROOT_QUESTION + HOME_URL + "{pageNumber}/{pageSize}")
  public ModelAndView getAllQuestions(@PathVariable Integer pageNumber,
      @PathVariable Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Direction.ASC, "question");
    Page<Question> results = questionService.getAllQuestionsOrderedByQuestion(pageable);
    ModelAndView model = new ModelAndView(ROOT_FOLDER + INDEX_VIEW_NAME);
    model.addObject("answersIteration",
        IntStream.range(0, 5)
            .boxed()
            .toArray(Integer[]::new));
    results.toList().get(0).getAnswers().get(0);
    return super.getAllResults(model, ROOT_QUESTION, pageNumber, pageSize, results);
  }

}
