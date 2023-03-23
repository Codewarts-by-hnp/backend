package com.codewarts.noriter.article.docs.question;

import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_MATCHED_WRITER;
import static com.codewarts.noriter.exception.type.ArticleExceptionType.DELETED_ARTICLE;
import static com.codewarts.noriter.exception.type.AuthExceptionType.EMPTY_ACCESS_TOKEN;
import static com.codewarts.noriter.exception.type.AuthExceptionType.TAMPERED_ACCESS_TOKEN;
import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static com.codewarts.noriter.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("질문게시판 상태 수정 기능 통합 테스트")
class QuestionUpdateStatusTest extends InitIntegrationRestDocsTest {

    @Test
    void Access_Token이_비어있는_경우_예외_발생() {
        String accessToken = " ";

        Map<String, Object> requestBody = Collections.singletonMap("status", "incomplete");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(requestBody)

        .when()
            .patch("/community/question/{id}")

        .then()
            .statusCode(EMPTY_ACCESS_TOKEN.getStatus().value())
            .body("errorCode", equalTo(EMPTY_ACCESS_TOKEN.getErrorCode()))
            .body("message", equalTo(EMPTY_ACCESS_TOKEN.getErrorMessage()));
    }

    @Test
    void Access_Token이_변조된_경우_예외_발생() {
        String accessToken = jwtProvider.issueAccessToken(1L) + "123";

        Map<String, Object> requestBody = Collections.singletonMap("status", "incomplete");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(requestBody)

        .when()
            .patch("/community/question/{id}")

        .then()
            .statusCode(TAMPERED_ACCESS_TOKEN.getStatus().value())
            .body("errorCode", equalTo(TAMPERED_ACCESS_TOKEN.getErrorCode()))
            .body("message", equalTo(TAMPERED_ACCESS_TOKEN.getErrorMessage()));
    }

    @Test
    void Path_Variable이_없는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Collections.singletonMap("status", "incomplete");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", " ")
            .body(requestBody)

        .when()
            .patch("/community/question/{id}")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("changeStatus.id: ID가 비어있습니다."));
    }

    @Test
    void id가_유효하지_않는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Collections.singletonMap("status", "incomplete");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -1)
            .body(requestBody)

        .when()
            .patch("/community/question/{id}")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("changeStatus.id: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void id가_존재하지_않는_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Collections.singletonMap("status", "incomplete");


        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 999999999)
            .body(requestBody)

        .when()
            .patch("/community/question/{id}")

        .then()
            .statusCode(ArticleExceptionType.ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void 필수값이_비어있을_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Collections.singletonMap("key", "incomplete");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(requestBody)

        .when()
            .patch("/community/question/{id}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo(INVALID_REQUEST.getErrorMessage()));
    }

    @Test
    void 존재하지_않는_회원인_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(99999999L);

        Map<String, Object> requestBody = Collections.singletonMap("status", "incomplete");


        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(requestBody)

        .when()
            .patch("/community/question/{id}")

        .then()
            .statusCode(MEMBER_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void 작성자가_일치하지_않는_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Collections.singletonMap("status", "incomplete");


        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
            .body(requestBody)

        .when()
            .patch("/community/question/{id}")

        .then()
            .statusCode(ARTICLE_NOT_MATCHED_WRITER.getStatus().value())
            .body("errorCode", equalTo(ARTICLE_NOT_MATCHED_WRITER.getErrorCode()));
    }

    @Test
    void 삭제된_게시물_상태변경을_요청할_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 15)
            .body(Collections.singletonMap("status", "incomplete"))

        .when()
            .patch("/community/question/{id}")

        .then()
            .statusCode(DELETED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(DELETED_ARTICLE.getErrorCode()))
            .body("message", equalTo(DELETED_ARTICLE.getErrorMessage()));
    }
}
