package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.dto.question.QuestionDetailResponse;
import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
import com.codewarts.noriter.article.domain.dto.question.QuestionListResponse;
import com.codewarts.noriter.article.domain.dto.question.QuestionUpdateRequest;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.article.repository.QuestionRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ArticleRepository articleRepository;
    private final MemberService memberService;
    private final ArticleUtils articleUtils;

    // 질문 등록 기능
    @Transactional
    public Long add(QuestionPostRequest request, Long memberId) {
        Member writer = memberService.findMember(memberId);
        Question question = request.toEntity(writer);
        question.addHashtags(request.getHashtags());
        Question savedQuestion = questionRepository.save(question);
        return savedQuestion.getId();
    }

    // 질문 조회 기능
    public List<QuestionListResponse> findList(StatusType status, String accessToken) {
        if (status == null) {
            return questionRepository.findAllQuestion().stream()
                .map(question -> new QuestionListResponse(question,
                    articleUtils.isSameWriter(question.getWriter().getId(), accessToken)))
                .collect(Collectors.toList());
        }
        return questionRepository.findQuestionByCompleted(status).stream()
            .map(question -> new QuestionListResponse(question,
                articleUtils.isSameWriter(question.getWriter().getId(), accessToken)))
            .collect(Collectors.toList());
    }

    // 질문 상세 조회 기능
    public QuestionDetailResponse findDetail(Long id, String accessToken) {
        Question question = findQuestion(id);
        boolean sameWriter = articleUtils.isSameWriter(question.getWriter().getId(), accessToken);
        return QuestionDetailResponse.from(question, sameWriter);
    }

    @Transactional
    public void delete(Long questionId, Long writerId) {
        memberService.findMember(writerId);
        Question question = findQuestion(questionId);
        question.validateWriterOrThrow(writerId);
        articleRepository.deleteByIdAndWriterId(questionId, writerId);
    }

    @Transactional
    public void update(Long questionId, Long writerId, QuestionUpdateRequest request) {
        memberService.findMember(writerId);
        Question question = findQuestion(questionId);
        question.validateWriterOrThrow(writerId);
        question.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    @Transactional
    public void updateStatus(Long questionId, Long writerId, StatusType status) {
        memberService.findMember(writerId);
        Question question = findQuestion(questionId);
        question.validateWriterOrThrow(writerId);

        if (status.equals(StatusType.COMPLETE)) {
            question.changeStatusToComplete();
            return;
        }
        question.changeStatusToIncomplete();
    }

    private Question findQuestion(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new GlobalNoriterException(
            ArticleExceptionType.ARTICLE_NOT_FOUND));
    }
}
