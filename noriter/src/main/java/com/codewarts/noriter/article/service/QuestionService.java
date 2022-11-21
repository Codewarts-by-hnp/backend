package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.dto.question.QuestionDetailResponse;
import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
import com.codewarts.noriter.article.domain.dto.question.QuestionResponse;
import com.codewarts.noriter.article.domain.dto.question.QuestionUpdateRequest;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.common.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final ArticleRepository questionRepository;
    private final MemberRepository memberRepository;

    // 질문 등록 기능
    @Transactional
    public Long add(QuestionPostRequest request, Long memberId) {
        Member writer = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        Question question = request.toEntity(writer);
        question.addHashtags(request.getHashtag());
        Question savedQuestion = questionRepository.save(question);
        return savedQuestion.getId();
    }

    // 질문 조회 기능
    public List<QuestionResponse> findList(Boolean status) {
        if (status == null) {
            return  questionRepository.findAllQuestion().stream().map(QuestionResponse::new).collect(Collectors.toList());
        }
        return questionRepository.findQuestionByCompleted(status).stream().map(QuestionResponse::new).collect(Collectors.toList());
    }

    // 질문 상세 조회 기능
    public QuestionDetailResponse findDetail(Long id) {
        Question question = questionRepository.findByQuestionId(id)
            .orElseThrow(RuntimeException::new);
        return QuestionDetailResponse.from(question);
    }

    @Transactional
    public void delete(Long questionId, Long writerId) {
        questionRepository.deleteByIdAndWriterId(questionId, writerId);
    }

    @Transactional
    public void update(Long id, Long writerId, QuestionUpdateRequest request) {
        Question findQuestion = questionRepository.findByQuestionIdAndWriterId(id, writerId)
            .orElseThrow(RuntimeException::new);
        findQuestion.update(request.getTitle(), request.getContent(), request.getHashtag());
    }
}
