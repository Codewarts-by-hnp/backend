package com.codewarts.noriter.article.repository;

import static com.codewarts.noriter.article.domain.QArticle.article;
import static com.codewarts.noriter.article.domain.QGathering.gathering;
import static com.codewarts.noriter.article.domain.QQuestion.question;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.Gathering;
import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Article> findAllPlayGroundList(Long memberId) {
        return queryFactory.selectFrom(article)
            .where(article.writer.id.eq(memberId))
            .fetch();
    }

    @Override
    public List<Gathering> findAllGatheringList(StatusType statusType, Long memberId) {
        return queryFactory.selectFrom(gathering)
            .where(
                gathering.writer.id.eq(memberId),
                gathering.status.eq(statusType)
            )
            .fetch();
    }

    @Override
    public List<Question> findAllQuestionList(StatusType statusType, Long memberId) {
        return queryFactory.selectFrom(question)
            .where(
                question.writer.id.eq(memberId),
                question.status.eq(statusType)
            )
            .fetch();
    }
}
