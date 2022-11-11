package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.domain.dto.study.StudyPostRequest;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public void register(StudyPostRequest studyPostRequest, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(IllegalArgumentException::new);
        Study study = studyPostRequest.toEntity(member);
        study.addHashtags(studyPostRequest.getHashtags());
        articleRepository.save(study);
    }
}
