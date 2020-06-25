package com.synoptic.project.quiz.manager.controller;

import com.synoptic.project.quiz.manager.model.Answer;
import com.synoptic.project.quiz.manager.model.Pagination;
import com.synoptic.project.quiz.manager.model.Question;
import com.synoptic.project.quiz.manager.model.Quiz;
import com.synoptic.project.quiz.manager.service.AnswerService;
import com.synoptic.project.quiz.manager.service.QuestionService;
import java.util.stream.IntStream;
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
public class QuestionController extends QuizManagerController<Question, Quiz> {

  public static final String ROOT_FOLDER = "questions";
  private final QuestionService questionService;
  private final AnswerService answerService;


  @Autowired
  public QuestionController(QuestionService questionService,
      AnswerService answerService) {
    this.questionService = questionService;
    this.answerService = answerService;
  }


  @GetMapping(ROOT_QUESTION)
  public RedirectView getQuestionHomePage() {
    return super.submitRedirect(ROOT_QUESTION + HOME_URL + Pagination.getDefaultURL());
  }

  @GetMapping(ROOT_QUESTION + HOME_URL + "{pageNumber}/{pageSize}")
  public ModelAndView getAllQuestions(@PathVariable Integer pageNumber,
      @PathVariable Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Direction.ASC, "id");
    Page<Question> results = questionService.getAllQuestionsOrderedByQuestion(pageable);
    ModelAndView model = new ModelAndView(ROOT_FOLDER + INDEX_VIEW_NAME);
    model.addObject("answersIteration",
        IntStream.range(0, 5)
            .boxed()
            .toArray(Integer[]::new));
    results.toList().get(0).getAnswers().get(0);
    return super.getAllResults(model, ROOT_QUESTION, pageNumber, pageSize, results);
  }

  @GetMapping(ROOT_QUESTION + VIEW_URL + "{id}")
  public ModelAndView viewQuestion(@PathVariable Integer id) {
    Question question = questionService.findQuestionById(id);

    String linkAction = ROOT_QUESTION;
    ModelAndView model = new ModelAndView(ROOT_FOLDER + EDIT_VIEW_NAME);

    model.addObject("result", question);
    model.addObject("name", "question");
    model.addObject("formType", "view");
    model.addObject("linkAction", linkAction + EDIT_URL);
    model.addObject("homeUri", linkAction);
    return model;
  }

  @GetMapping(ROOT_QUESTION + EDIT_URL + "{id}")
  public ModelAndView editQuestion(@PathVariable Integer id) {
    ModelAndView model = new ModelAndView(ROOT_FOLDER + EDIT_VIEW_NAME);
    Question question = questionService.findQuestionById(id);
    String linkAction = ROOT_QUESTION;

    model.addObject("result", question);
    model.addObject("name", "question");
    model.addObject("formType", "edit");
    model.addObject("linkAction", linkAction + UPDATE_URL);
    model.addObject("homeUri", linkAction);
    model.addObject("addAnswerAvailable", question.getAnswers().size() < 5);
    model.addObject("addAnswerLink", ROOT_QUESTION + EDIT_URL + ADD_ANSWER_URL);
    model.addObject("deleteAnswerLink", ROOT_QUESTION + EDIT_URL + DELETE_ANSWER_URL);
    return model;
  }

  @GetMapping(ROOT_QUESTION + EDIT_URL + ADD_ANSWER_URL + "{id}")
  public RedirectView addAnswer(@PathVariable Integer id) {
    Question question = questionService.findQuestionById(id);
    Answer answer = answerService.updateOrCreateAnswer(new Answer());
    answer.setQuestion(question);
    question.getAnswers().add(answer);
    questionService.updateOrCreateQuestion(question);
    return super.submitRedirect(ROOT_QUESTION + EDIT_URL + id);
  }

  @GetMapping(ROOT_QUESTION + EDIT_URL + DELETE_ANSWER_URL + "{id}")
  public RedirectView deleteAnswer(@PathVariable Integer id) {
    Answer answer = answerService.findAnswerById(id);
    Integer questionId = answer.getQuestion().getId();
    Question question = questionService.findQuestionById(questionId);

    question.getAnswers().remove(answer);
    answer.setQuestion(null);
//    answerService.updateOrCreateAnswer(answer);
//    questionService.updateOrCreateQuestion(question);
    answerService.deleteAnswerById(id);
//    TODO: How To:
//     [ ] bind answer ID to delete button,
//     [ ] retrieve answer,
//     [ ] set answer question id for redirect,
//     [ ] clear question from answer,
//     [ ] clear answer from question,
//     [ ] delete answer.
    return super.submitRedirect(ROOT_QUESTION + EDIT_URL + questionId);
  }

  @GetMapping(ROOT_QUESTION + ADD_URL)
  public ModelAndView addQuestion() {

    return new ModelAndView(ROOT_FOLDER + NEW_VIEW_NAME);
  }

  @PostMapping(ROOT_QUESTION + UPDATE_URL + "{id}")
  public RedirectView updateQuestion(@PathVariable Integer id, @ModelAttribute Question question) {
    Question questionRef = questionService.findQuestionById(id);

    for (int i = 0; i < questionRef.getAnswers().size(); i++) {
      Answer answerRef = questionRef.getAnswers().get(i);
      Answer answer = question.getAnswers().get(i);
      answerRef.setAnswer(answer.getAnswer());
      answerRef.setCorrectAnswer(answer.isCorrectAnswer());
    }

    questionRef.setQuestion(question.getQuestion());
    answerService.updateAnswersOnQuestion(questionRef);
    questionService.updateOrCreateQuestion(questionRef);
    return super.submitRedirect(ROOT_QUESTION + VIEW_URL + id);
  }

  @PostMapping(ROOT_QUESTION + ADD_URL)
  public RedirectView createQuestion(@ModelAttribute Question question) {
    Question newQuestion = questionService.updateOrCreateQuestion(question);
    return super.submitRedirect(ROOT_QUESTION + VIEW_URL + newQuestion.getId());
  }

  @PostMapping(ROOT_QUESTION + DELETE_URL + "{id}")
  public RedirectView deleteQuestion(@PathVariable int id) {
    questionService.deleteQuestionById(id);
    return submitRedirect(ROOT_QUIZ);
  }

}
