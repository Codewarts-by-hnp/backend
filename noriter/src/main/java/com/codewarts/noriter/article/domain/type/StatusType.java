package com.codewarts.noriter.article.domain.type;

import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusType {

    COMPLETE, INCOMPLETE;

    @JsonCreator
    public static StatusType valueOfWithCaseInsensitive(String type) {
        type = type.toUpperCase();

        try {
            return StatusType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new GlobalNoriterException(CommonExceptionType.INCORRECT_REQUEST_VALUE);
        }
    }
}
