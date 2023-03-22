package com.codewarts.noriter.article.docs.comment;

import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_FOUND;
import static com.codewarts.noriter.exception.type.ArticleExceptionType.DELETED_ARTICLE;
import static com.codewarts.noriter.exception.type.AuthExceptionType.EMPTY_ACCESS_TOKEN;
import static com.codewarts.noriter.exception.type.AuthExceptionType.TAMPERED_ACCESS_TOKEN;
import static com.codewarts.noriter.exception.type.CommentExceptionType.COMMENT_NOT_FOUND;
import static com.codewarts.noriter.exception.type.CommentExceptionType.DELETED_COMMENT;
import static com.codewarts.noriter.exception.type.CommentExceptionType.NOT_MATCHED_ARTICLE;
import static com.codewarts.noriter.exception.type.CommentExceptionType.NOT_MATCHED_WRITER;
import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static com.codewarts.noriter.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("댓글 수정 기능 통합 테스트")
class CommentUpdateTest extends InitIntegrationRestDocsTest {

    @Test
    void 댓글을_수정한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void Access_Token이_비어있는_경우_예외_발생() {
        String accessToken = " ";

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(EMPTY_ACCESS_TOKEN.getStatus().value())
            .body("errorCode", equalTo(EMPTY_ACCESS_TOKEN.getErrorCode()))
            .body("message", equalTo(EMPTY_ACCESS_TOKEN.getErrorMessage()));
    }

    @Test
    void Access_Token이_변조된_경우_예외_발생() {
        String accessToken = jwtProvider.issueAccessToken(1L) + "123";

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(TAMPERED_ACCESS_TOKEN.getStatus().value())
            .body("errorCode", equalTo(TAMPERED_ACCESS_TOKEN.getErrorCode()))
            .body("message", equalTo(TAMPERED_ACCESS_TOKEN.getErrorMessage()));
    }

    @Test
    void 존재하지_않는_회원인_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(99999999L);

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(MEMBER_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void 필수값이_비어있을_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", " ");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo(INVALID_REQUEST.getErrorMessage()))
            .body("detail.content", equalTo("내용은 필수입니다."))
            .body("detail.secret", equalTo("내용은 필수입니다."));
    }

    @Test
    void articleId가_Null이면_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", " ")
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editComment.articleId: ID가 비어있습니다."));
    }

    @Test
    void articleId가_음수이면_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", -1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editComment.articleId: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void articleId가_존재하지_않는_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 9999999)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ARTICLE_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void articleId가_삭제된_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 13)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(DELETED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(DELETED_ARTICLE.getErrorCode()))
            .body("message", equalTo(DELETED_ARTICLE.getErrorMessage()));
    }

    @Test
    void id가_Null이면_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", " ")
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editComment.id: ID가 비어있습니다."));
    }

    @Test
    void id가_음수이면_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", -1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editComment.id: 댓글 ID는 양수이어야 합니다."));
    }

    @Test
    void id가_존재하지_않는_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "수정한 댓글이에요",
            "secret", "false");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", 9999999)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(COMMENT_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(COMMENT_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(COMMENT_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void id가_삭제된_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 12)
            .pathParam("id", 6)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(DELETED_COMMENT.getStatus().value())
            .body("errorCode", equalTo(DELETED_COMMENT.getErrorCode()))
            .body("message", equalTo(DELETED_COMMENT.getErrorMessage()));
    }

    @Test
    void 작성자가_일치하지_않는_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(NOT_MATCHED_WRITER.getStatus().value())
            .body("errorCode", equalTo(NOT_MATCHED_WRITER.getErrorCode()))
            .body("message", equalTo(NOT_MATCHED_WRITER.getErrorMessage()));
    }

    @Test
    void 해당_게시글의_댓글이_아닌_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 2)
            .pathParam("id", 1)
            .body(requestBody)

        .when()
            .put("/{articleId}/comment/{id}")

        .then()
            .statusCode(NOT_MATCHED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(NOT_MATCHED_ARTICLE.getErrorCode()))
            .body("message", equalTo(NOT_MATCHED_ARTICLE.getErrorMessage()));
    }
}
