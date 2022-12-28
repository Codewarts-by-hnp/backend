package com.codewarts.noriter.article.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusType {

    SOLVED, UNSOLVED, COMPLETE, INCOMPLETE;

}
