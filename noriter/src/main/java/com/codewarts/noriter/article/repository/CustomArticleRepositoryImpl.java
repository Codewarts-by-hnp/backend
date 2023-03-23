package com.codewarts.noriter.article.repository;

import static com.codewarts.noriter.article.domain.QGathering.gathering;
import static com.codewarts.noriter.article.domain.QQuestion.question;

import com.codewarts.noriter.article.domain.Gathering;
import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Gathering> findAllGatheringList(StatusType statusType) {
        return queryFactory.selectFrom(gathering)
            .where(
                isSameStatusTypeGathering(statusType),
                gathering.deleted.isNull()
            )
            .fetch();
    }

    @Override
    public List<Question> findAllQuestionList(StatusType statusType) {
        return queryFactory.selectFrom(question)
            .where(
                isSameStatusTypeQuestion(statusType),
                question.deleted.isNull()
            )
            .fetch();
    }

    private BooleanExpression isSameStatusTypeGathering(StatusType statusType) {
        return ObjectUtils.isEmpty(statusType) ? null : gathering.status.eq(statusType);
    }

    private BooleanExpression isSameStatusTypeQuestion(StatusType statusType) {
        return ObjectUtils.isEmpty(statusType) ? null : question.status.eq(statusType);
    }
}
