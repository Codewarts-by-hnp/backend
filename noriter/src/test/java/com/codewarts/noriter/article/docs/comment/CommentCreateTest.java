package com.codewarts.noriter.article.docs.comment;

import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_FOUND;
import static com.codewarts.noriter.exception.type.AuthExceptionType.EMPTY_ACCESS_TOKEN;
import static com.codewarts.noriter.exception.type.AuthExceptionType.TAMPERED_ACCESS_TOKEN;
import static com.codewarts.noriter.exception.type.CommonExceptionType.INVALID_REQUEST;
import static com.codewarts.noriter.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
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
class CommentCreateTest {

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
    void 댓글을_등록한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void Access_Token이_비어있는_경우_예외_발생() {
        String accessToken = " ";

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment")

            .then()
            .statusCode(EMPTY_ACCESS_TOKEN.getStatus().value())
            .body("errorCode", equalTo(EMPTY_ACCESS_TOKEN.getErrorCode()))
            .body("message", equalTo(EMPTY_ACCESS_TOKEN.getErrorMessage()));
    }

    @Test
    void Access_Token이_변조된_경우_예외_발생() {
        String accessToken = jwtProvider.issueAccessToken(1L) + "123";

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment")

            .then()
            .statusCode(TAMPERED_ACCESS_TOKEN.getStatus().value())
            .body("errorCode", equalTo(TAMPERED_ACCESS_TOKEN.getErrorCode()))
            .body("message", equalTo(TAMPERED_ACCESS_TOKEN.getErrorMessage()));
    }

    @Test
    void 존재하지_않는_회원인_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(99999999L);

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getErrorMessage()));
    }

    @Test
    void 필수값이_비어있을_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment")

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

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", " ")
            .body(requestBody)

            .when()
            .post("/{articleId}/comment")
            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerComment.articleId: ID가 비어있습니다."));
    }

    @Test
    void articleId가_음수이면_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", -1)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment")
            .then()
            .statusCode(INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("registerComment.articleId: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void articleId가_존재하지_않는_경우_예외가_발생한다() {
        String accessToken = jwtProvider.issueAccessToken(1L);

        Map<String, Object> requestBody = Map.of("content", "댓글이에요",
            "secret", "true");

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("articleId", 99999999)
            .body(requestBody)

            .when()
            .post("/{articleId}/comment")
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
            .body(requestBody)

            .when()
            .post("/{articleId}/comment")
            .then()
            .statusCode(ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ARTICLE_NOT_FOUND.getErrorMessage()));
    }
}
