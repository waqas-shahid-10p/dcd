package com.crossover.dto;

public class RepoDTO {

  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(final String url) {
    this.url = url;
  }

  public RepoDTO withUrl(final String url) {
    this.url = url;
    return this;
  }
}
