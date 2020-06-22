package com.synoptic.project.quiz.manager.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"answers", "quizzes"})
public class Question {

  @ManyToMany(mappedBy = "questions", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  List<Quiz> quizzes;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column
  private String question;
  @OneToMany(mappedBy = "question")
  private List<Answer> answers;

}
