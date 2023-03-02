package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.dto.article.ArticleCreateRequest;
import com.codewarts.noriter.article.dto.article.ArticleListResponse;
import com.codewarts.noriter.article.dto.article.ArticleUpdateRequest;
import com.codewarts.noriter.article.dto.question.QuestionDetailResponse;
import com.codewarts.noriter.article.dto.question.QuestionListResponse;
import com.codewarts.noriter.article.repository.QuestionRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.service.MemberService;
import com.codewarts.noriter.wish.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService extends ArticleService {

    private final QuestionRepository questionRepository;
    private final WishRepository wishRepository;
    private final MemberService memberService;

    // 질문 등록 기능
    @Override
    @Transactional
    public Long create(ArticleCreateRequest request, Long memberId) {
        Member writer = memberService.findMember(memberId);
        Question question = (Question) request.toEntity(writer);
        return questionRepository.save(question).getId();
    }

    // 질문 조회 기능
    @Override
    public List<ArticleListResponse> findList(StatusType status, Long memberId) {
        if (memberId == null && status == null) {
            return questionRepository.findAllQuestion().stream()
                .map(question -> new QuestionListResponse(question, false, false))
                .collect(Collectors.toList());
        }
        if (memberId == null) {
            return questionRepository.findQuestionByCompleted(status).stream()
                .map(question -> new QuestionListResponse(question, false, false))
                .collect(Collectors.toList());
        }
        Member member = memberService.findMember(memberId);
        if (status == null) {
            return questionRepository.findAllQuestion().stream()
                .map(question -> new QuestionListResponse(question,
                    question.getWriter().getId().equals(memberId),
                    wishRepository.existsByArticleAndMember(question, member)))
                .collect(Collectors.toList());
        }
        return questionRepository.findQuestionByCompleted(status).stream()
            .map(question -> new QuestionListResponse(question,
                question.getWriter().getId().equals(memberId),
                wishRepository.existsByArticleAndMember(question, member)))
            .collect(Collectors.toList());
    }

    // 질문 상세 조회 기능
    @Override
    public QuestionDetailResponse findDetail(Long id, Long memberId) {
        Question question = findNotDeletedQuestion(id);
        boolean sameWriter = question.getWriter().getId().equals(memberId);

        if (memberId == null) {
            return QuestionDetailResponse.from(question, false, false);
        }
        Member member = memberService.findMember(memberId);
        boolean wish = wishRepository.existsByArticleAndMember(question, member);

        return QuestionDetailResponse.from(question, sameWriter, wish);
    }

    @Override
    @Transactional
    public void delete(Long questionId, Long writerId) {
        memberService.findMember(writerId);
        Question question = findNotDeletedQuestion(questionId);
        question.validateWriterOrThrow(writerId);
        question.delete();
    }

    @Override
    @Transactional
    public void update(Long questionId, ArticleUpdateRequest request, Long writerId) {
        memberService.findMember(writerId);
        Question question = findNotDeletedQuestion(questionId);
        question.validateWriterOrThrow(writerId);
        question.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    @Transactional
    public void updateStatus(Long questionId, Long writerId, StatusType status) {
        memberService.findMember(writerId);
        Question question = findNotDeletedQuestion(questionId);
        question.validateWriterOrThrow(writerId);

        if (status.equals(StatusType.COMPLETE)) {
            question.changeStatusToComplete();
            return;
        }
        question.changeStatusToIncomplete();
    }

    private Question findNotDeletedQuestion(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(
                ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (question.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.DELETED_ARTICLE);
        }
        return question;
    }
}
