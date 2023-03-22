package com.codewarts.noriter.article.docs.comment;

import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommentExceptionType;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("대댓글 등록기능 통합 테스트")
public class ReCommentCreateTest extends InitIntegrationRestDocsTest {
    @Test
    void 대댓을_등록한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .post("/{articleId}/comment/{id}/recomment")

        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 필드값이_비어있을_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "잘 보셨다니 다행입니다.");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .post("/{articleId}/comment/{id}/recomment")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo(INVALID_REQUEST.getErrorMessage()))
            .body("detail.secret", equalTo("내용은 필수입니다."));
    }

    @Test
    void articleId가_null인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", " ")
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .post("/{articleId}/comment/{id}/recomment")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerReComment.articleId: ID가 비어있습니다."));
    }

    @Test
    void articleId가_음수인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", -1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .post("/{articleId}/comment/{id}/recomment")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerReComment.articleId: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void commentId가_null인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", " ")
            .body(requestBody)

        .when()
            .post("/{articleId}/comment/{id}/recomment")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerReComment.id: ID가 비어있습니다."));
    }

    @Test
    void commentId가_음수인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", -1)
            .body(requestBody)

        .when()
            .post("/{articleId}/comment/{id}/recomment")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerReComment.id: 댓글 ID는 양수이어야 합니다."));
    }

    @Test
    void 삭제된_게시글일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 13)
            .pathParam("id", 7)
            .body(requestBody)

        .when()
            .post("/{articleId}/comment/{id}/recomment")

        .then()
            .statusCode(ArticleExceptionType.DELETED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(ArticleExceptionType.DELETED_ARTICLE.getErrorCode()))
            .body("message", equalTo(ArticleExceptionType.DELETED_ARTICLE.getErrorMessage()));
    }

    @Test
    void 대댓글의_대댓글을_요청할_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 12)
            .pathParam("id", 13)
            .body(requestBody)

        .when()
            .post("/{articleId}/comment/{id}/recomment")

        .then()
            .statusCode(CommentExceptionType.DELETED_COMMENT.getStatus().value())
            .body("errorCode", equalTo(CommentExceptionType.NOT_ALLOWED_RECOMMENT.getErrorCode()))
            .body("message", equalTo(CommentExceptionType.NOT_ALLOWED_RECOMMENT.getErrorMessage()));
    }
}
