package com.champlain.artistservice.utils.exceptions;

public class DuplicateStageNameException extends RuntimeException {
  public DuplicateStageNameException(String message) {
    super(message);
  }
}
