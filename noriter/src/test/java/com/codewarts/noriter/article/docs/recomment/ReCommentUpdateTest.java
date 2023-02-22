package com.codewarts.noriter.article.docs.recomment;

import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.DATE;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommentExceptionType;
import com.codewarts.noriter.exception.type.ReCommentExceptionType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile({"test"})
@Sql("classpath:/data.sql")
public class ReCommentUpdateTest {


    @LocalServerPort
    int port;

    @Autowired
    protected JwtProvider jwtProvider;

    protected RequestSpecification documentationSpec;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        documentationSpec = new RequestSpecBuilder()
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        prettyPrint(),
                        removeHeaders(HOST, CONTENT_LENGTH))
                    .withResponseDefaults(
                        prettyPrint(),
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING,
                            "Keep-Alive",
                            HttpHeaders.VARY))
            )
            .build();
    }

    @Test
    void 대댓글을_수정한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "대댓글을 수정해볼게용",
            "secret", false);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

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
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
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
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editReComment.articleId: ID가 비어있습니다."));
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
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editReComment.articleId: 게시글 ID는 양수이어야 합니다."));
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
            .pathParam("commentId", " ")
            .pathParam("recommentId", 1)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editReComment.commentId: ID가 비어있습니다."));
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
            .pathParam("commentId", -1)
            .pathParam("recommentId", 1)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editReComment.commentId: 댓글 ID는 양수이어야 합니다."));
    }

    @Test
    void id가_null인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", " ")
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editReComment.recommentId: ID가 비어있습니다."));
    }

    @Test
    void id가_음수인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", -1)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("editReComment.recommentId: 대댓글 ID는 양수이어야 합니다."));
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
            .pathParam("commentId", 7)
            .pathParam("recommentId", 4)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(ArticleExceptionType.DELETED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(ArticleExceptionType.DELETED_ARTICLE.getErrorCode()))
            .body("message", equalTo(ArticleExceptionType.DELETED_ARTICLE.getErrorMessage()));
    }

    @Test
    void 삭제된_댓글일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 12)
            .pathParam("commentId", 6)
            .pathParam("recommentId", 2)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(CommentExceptionType.DELETED_COMMENT.getStatus().value())
            .body("errorCode", equalTo(CommentExceptionType.DELETED_COMMENT.getErrorCode()))
            .body("message", equalTo(CommentExceptionType.DELETED_COMMENT.getErrorMessage()));
    }

    @Test
    void 삭제된_대댓글일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 5)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(ReCommentExceptionType.DELETED_RECOMMENT.getStatus().value())
            .body("errorCode", equalTo(ReCommentExceptionType.DELETED_RECOMMENT.getErrorCode()))
            .body("message", equalTo(ReCommentExceptionType.DELETED_RECOMMENT.getErrorMessage()));
    }

    @Test
    void 글쓴이가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "안녕하세용",
            "secret", true);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(ReCommentExceptionType.RECOMMENT_NOT_MATCHED_WRITER.getStatus().value())
            .body("errorCode", equalTo(ReCommentExceptionType.RECOMMENT_NOT_MATCHED_WRITER.getErrorCode()))
            .body("message", equalTo(ReCommentExceptionType.RECOMMENT_NOT_MATCHED_WRITER.getErrorMessage()));
    }

    @Test
    void 해당게시글의_댓글이_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "대댓글을 수정해볼게용",
            "secret", false);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 2)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 1)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(CommentExceptionType.NOT_MATCHED_ARTICLE.getStatus().value())
            .body("errorCode", equalTo(CommentExceptionType.NOT_MATCHED_ARTICLE.getErrorCode()))
            .body("message", equalTo(CommentExceptionType.NOT_MATCHED_ARTICLE.getErrorMessage()));
    }

    @Test
    void 해당댓글의_대댓글이_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "대댓글을 수정해볼게용",
            "secret", false);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .pathParam("commentId", 1)
            .pathParam("recommentId", 2)
            .body(requestBody)

            .when()
            .put("/{articleId}/comment/{commentId}/recomment/{recommentId}")

            .then()
            .statusCode(ReCommentExceptionType.NOT_MATCHED_COMMENT.getStatus().value())
            .body("errorCode", equalTo(ReCommentExceptionType.NOT_MATCHED_COMMENT.getErrorCode()))
            .body("message", equalTo(ReCommentExceptionType.NOT_MATCHED_COMMENT.getErrorMessage()));
    }
}
