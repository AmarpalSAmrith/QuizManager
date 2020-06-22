package com.synoptic.project.quiz.manager.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"questions"})
public class Quiz {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "quiz_questions",
      joinColumns = @JoinColumn(name = "quiz_id"),
      inverseJoinColumns = @JoinColumn(name = "question_id"))
  private List<Question> questions;

}
