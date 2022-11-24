package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.dto.free.FreePostRequest;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FreeService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(FreePostRequest freePostRequest, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        Article free = freePostRequest.toEntity(member);
        free.addHashtags(freePostRequest.getHashtags());
        articleRepository.save(free);
    }
}
