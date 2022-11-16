package com.codewarts.noriter.article.exception.question;

public class NoSuchQuestionException extends RuntimeException {
    public NoSuchQuestionException() {
        super("존재하지 않는 질문입니다.");
    }
}
