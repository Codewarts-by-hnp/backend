package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.dto.article.ArticleCreateRequest;
import com.codewarts.noriter.article.dto.article.ArticleDetailResponse;
import com.codewarts.noriter.article.dto.article.ArticleListResponse;
import com.codewarts.noriter.article.dto.article.ArticleUpdateRequest;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.MemberExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public abstract class ArticleService {

    private MemberRepository memberRepository;

    Long create(ArticleCreateRequest request, Long writerId) {
        return null;
    }


    ArticleDetailResponse findDetail(Long id, Long memberId) {
        return null;
    }


    List<ArticleListResponse> findList(Long memberId) {
        return null;
    }

    List<ArticleListResponse> findList(StatusType status, Long memberId) {
        return null;
    }

    void delete(Long id, Long writerId) {
    }

    void update(Long id, ArticleUpdateRequest request, Long writerId) {

    }

     Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GlobalNoriterException(
            MemberExceptionType.MEMBER_NOT_FOUND));
    }
}
