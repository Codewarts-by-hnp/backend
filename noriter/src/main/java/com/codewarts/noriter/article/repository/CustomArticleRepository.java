package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Gathering;
import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.StatusType;
import java.util.List;

public interface CustomArticleRepository {
    List<Gathering> findAllGatheringList(StatusType statusType);

    List<Question> findAllQuestionList(StatusType statusType);
}
