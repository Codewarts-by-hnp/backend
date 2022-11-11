package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
import com.codewarts.noriter.article.exception.member.NoSuchMemberException;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final ArticleRepository questionRepository;
    private final MemberRepository memberRepository;

    // 질문 등록하기
    @Transactional
    public Long add(QuestionPostRequest request, Long memberId) {
        Member writer = memberRepository.findById(memberId).orElseThrow(NoSuchMemberException::new);
        Question question = request.toEntity(writer);
        question.addHashtags(request.getHashtag());
        Question savedQuestion = questionRepository.save(question);
        return savedQuestion.getId();
    }
}
