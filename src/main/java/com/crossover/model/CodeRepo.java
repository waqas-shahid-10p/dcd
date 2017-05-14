package com.crossover.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
public class CodeRepo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @URL(regexp = "(?:git|ssh|https?|git@[-\\w.]+):(\\/\\/)?(.*?)(\\.git)(\\/?|\\#[-\\d\\w._]+?)$")
  @NotBlank
  @Column(nullable = false, unique = true)
  private String url;
  @NotNull
  @Column(nullable = false)
  private Status status = Status.ADDED;
  @OneToMany(mappedBy = "repo", cascade = CascadeType.ALL)
  private List<DeadCodeOccurrence> deadCodeOccurrences;
  @NotNull
  @Column(nullable = false)
  private Date createdAt = new Date();
  private Date updatedAt;

  public CodeRepo() {
  }

  public CodeRepo(final String url) {
    this.url = url;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public CodeRepo withId(long id) {
    setId(id);
    return this;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public CodeRepo withStatus(Status status) {
    setStatus(status);
    return this;
  }

  @JsonIgnore
  public List<DeadCodeOccurrence> getDeadCodeOccurrences() {
    return deadCodeOccurrences;
  }

  public void setDeadCodeOccurrences(
      List<DeadCodeOccurrence> deadCodeOccurrences) {
    this.deadCodeOccurrences = deadCodeOccurrences;
  }

  public CodeRepo withDeadCodeOccurrences(
      List<DeadCodeOccurrence> deadCodeOccurrences) {
    setDeadCodeOccurrences(deadCodeOccurrences);
    return this;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && obj instanceof CodeRepo && ((CodeRepo) obj).id == this.id;
  }

  public enum Status {
    ADDED, PROCESSING, COMPLETED, FAILED
  }
}