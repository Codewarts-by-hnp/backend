package com.codewarts.noriter.article.docs.recomment;

import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommentExceptionType;
import com.codewarts.noriter.exception.type.ReCommentExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


@DisplayName("대댓글 삭제 기능 통합 테스트")
public class ReCommentDeleteTest extends InitIntegrationRestDocsTest {

    @Test
    void 대댓글을_삭제한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void articleId가_null인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", " ")
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("removeReComment.articleId: ID가 비어있습니다."));
    }

    @Test
    void articleId가_음수인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", -1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("removeReComment.articleId: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void commentId가_null인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", " ")
            .pathParam("recommentId", 1)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("removeReComment.commentId: ID가 비어있습니다."));
    }

    @Test
    void commentId가_음수인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", -1)
            .pathParam("recommentId", 1)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("removeReComment.commentId: 댓글 ID는 양수이어야 합니다."));
    }

    @Test
    void id가_null인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", " ")

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("removeReComment.recommentId: ID가 비어있습니다."));
    }

    @Test
    void id가_음수인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", -1)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("removeReComment.recommentId: 대댓글 ID는 양수이어야 합니다."));
    }

    @Test
    void 삭제된_게시글일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 13)
            .pathParam("commentId", 7)
            .pathParam("recommentId", 4)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(ArticleExceptionType.DELETED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(ArticleExceptionType.DELETED_ARTICLE.getErrorCode()))
            .body("message", equalTo(ArticleExceptionType.DELETED_ARTICLE.getErrorMessage()));
    }

    @Test
    void 삭제된_댓글일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 12)
            .pathParam("commentId", 6)
            .pathParam("recommentId", 2)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(CommentExceptionType.DELETED_COMMENT.getStatus().value())
            .body("errorCode", equalTo(CommentExceptionType.DELETED_COMMENT.getErrorCode()))
            .body("message", equalTo(CommentExceptionType.DELETED_COMMENT.getErrorMessage()));
    }

    @Test
    void 삭제된_대댓글일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 5)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(ReCommentExceptionType.DELETED_RECOMMENT.getStatus().value())
            .body("errorCode", equalTo(ReCommentExceptionType.DELETED_RECOMMENT.getErrorCode()))
            .body("message", equalTo(ReCommentExceptionType.DELETED_RECOMMENT.getErrorMessage()));
    }

    @Test
    void 글쓴이가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(ReCommentExceptionType.RECOMMENT_NOT_MATCHED_WRITER.getStatus().value())
            .body("errorCode", equalTo(ReCommentExceptionType.RECOMMENT_NOT_MATCHED_WRITER.getErrorCode()))
            .body("message", equalTo(ReCommentExceptionType.RECOMMENT_NOT_MATCHED_WRITER.getErrorMessage()));
    }

    @Test
    void 해당게시글의_댓글이_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 2)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(CommentExceptionType.NOT_MATCHED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(CommentExceptionType.NOT_MATCHED_ARTICLE.getErrorCode()))
            .body("message", equalTo(CommentExceptionType.NOT_MATCHED_ARTICLE.getErrorMessage()));
    }

    @Test
    void 해당댓글의_대댓글이_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 2)

        .when()
            .delete("/{articleId}/comment/{commentId}/recomment/{recommentId}")

        .then()
            .statusCode(ReCommentExceptionType.NOT_MATCHED_COMMENT.getStatus().value())
            .body("errorCode", equalTo(ReCommentExceptionType.NOT_MATCHED_COMMENT.getErrorCode()))
            .body("message", equalTo(ReCommentExceptionType.NOT_MATCHED_COMMENT.getErrorMessage()));
    }
}
