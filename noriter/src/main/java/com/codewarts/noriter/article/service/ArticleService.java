package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.dto.article.ArticleCreateRequest;
import com.codewarts.noriter.article.dto.article.ArticleDetailResponse;
import com.codewarts.noriter.article.dto.article.ArticleListResponse;
import com.codewarts.noriter.article.dto.article.ArticleUpdateRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public abstract class ArticleService {


    Long create(ArticleCreateRequest request, Long writerId) {
        return null;
    }


    ArticleDetailResponse findDetail(Long id, Long memberId) {
        return null;
    }


    List<ArticleListResponse> findList(Long memberId) {
        return null;
    }

    List<ArticleListResponse> findList(Long memberId, StatusType status) {
        return null;
    }

    void delete(Long id, Long writerId) {
    }

    void update(Long id, ArticleUpdateRequest request, Long writerId) {

    }
}
