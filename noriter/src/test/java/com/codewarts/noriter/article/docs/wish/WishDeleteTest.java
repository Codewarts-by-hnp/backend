package com.codewarts.noriter.article.docs.wish;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import com.codewarts.noriter.exception.type.WishExceptionType;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


@DisplayName("찜 취소 기능 통합 테스트")
class WishDeleteTest extends InitIntegrationRestDocsTest {

    @Test
    void 찜을_취소한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("articleId", 3))

        .when()
            .delete("/wish")

        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void articleId가_null인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("articleId", null))

        .when()
            .delete("/wish")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("remove.map[articleId].<map value>: ID가 비어있습니다."));
    }

    @Test
    void 찜하지_않은_게시글을_요청했을_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("articleId", 2))

        .when()
            .delete("/wish")

        .then()
            .statusCode(WishExceptionType.CANNOT_CANCEL_NOT_EXIST_WISH.getStatus().value())
            .body("errorCode", equalTo(WishExceptionType.CANNOT_CANCEL_NOT_EXIST_WISH.getErrorCode()))
            .body("message", equalTo(WishExceptionType.CANNOT_CANCEL_NOT_EXIST_WISH.getErrorMessage()));
    }

    @Test
    void articleId가_유효하지_않는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("articleId", -2))

        .when()
            .delete("/wish")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("remove.map[articleId].<map value>: 게시글 ID는 양수이어야 합니다."));
    }


    @Test
    void articleId가_존재하지_않는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("articleId", 999999))

        .when()
            .delete("/wish")

        .then()
            .statusCode(ArticleExceptionType.ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorMessage()));
    }
}
