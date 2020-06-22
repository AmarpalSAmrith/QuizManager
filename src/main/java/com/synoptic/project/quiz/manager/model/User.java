package com.synoptic.project.quiz.manager.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  private String username;

  @Column
  private String firstName;

  private String lastName;

  private String password;

  private Boolean active;

  private String email;

  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  private List<Authority> authorities;

}
