package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.dto.free.FreeDetailResponse;
import com.codewarts.noriter.article.domain.dto.free.FreeEditRequest;
import com.codewarts.noriter.article.domain.dto.free.FreeListResponse;
import com.codewarts.noriter.article.domain.dto.free.FreePostRequest;
import com.codewarts.noriter.article.domain.type.ArticleType;
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
public class FreeService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(FreePostRequest freePostRequest, Long writerId) {
        Member member = memberRepository.findById(writerId).orElseThrow(RuntimeException::new);
        Article free = freePostRequest.toEntity(member);
        free.addHashtags(freePostRequest.getHashtags());
        articleRepository.save(free);
    }

    public FreeDetailResponse findDetail(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(RuntimeException::new);
        return new FreeDetailResponse(article);
    }

    public List<FreeListResponse> findList() {
        List<Article> freeTypeArticle = articleRepository.findAllByArticleType(ArticleType.FREE);
        return freeTypeArticle.stream().map(FreeListResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void update(Long id, FreeEditRequest request, Long writerId) {
        Article free = articleRepository.findByIdAndWriterId(id, writerId);
        free.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    public void delete(Long id, Long writerId) {
        articleRepository.deleteByIdAndWriterId(id, writerId);
    }
}
