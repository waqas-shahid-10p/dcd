package com.crossover.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class DeadCodeOccurrence {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(cascade = CascadeType.ALL)
  private CodeRepo repo;

  @Column(nullable = false)
  @NotBlank
  private String value;
  @Column(nullable = false)
  @NotBlank
  private String file;
  @Column(nullable = false)
  @NotBlank
  private String line;
  @Column(nullable = false)
  @NotBlank
  private String kind;

  @JsonIgnore
  public CodeRepo getRepo() {
    return repo;
  }

  public void setRepo(CodeRepo repo) {
    this.repo = repo;
  }

  public DeadCodeOccurrence withRepo(final CodeRepo repo) {
    this.setRepo(repo);
    return this;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public DeadCodeOccurrence withValue(String value) {
    setValue(value);
    return this;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public DeadCodeOccurrence withFile(String file) {
    setFile(file);
    return this;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public DeadCodeOccurrence withLine(String line) {
    setLine(line);
    return this;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public DeadCodeOccurrence withKind(String kind) {
    setKind(kind);
    return this;
  }
}
