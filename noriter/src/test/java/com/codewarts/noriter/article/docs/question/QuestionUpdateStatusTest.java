package com.codewarts.noriter.article.docs.question;

import static com.codewarts.noriter.exception.type.ArticleExceptionType.ARTICLE_NOT_MATCHED_WRITER;
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

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.repository.QuestionRepository;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionUpdateStatusTest {

    @LocalServerPort
    int port;
    @Autowired
    private QuestionRepository questionRepository;
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

    @ParameterizedTest
    @ValueSource(strings = {"complete", "incomplete"})
    void 해결_여부를_변경한다(String status) {

        Question question = questionRepository.findById(6L)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        String accessToken = jwtProvider.issueAccessToken(question.getWriter().getId());

        Map<String, String> map = Collections.singletonMap("status", status);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(map)

            .when()
            .patch("/community/question/6")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

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
            .body("message", equalTo("questionChangeStatus.id: ID가 비어있습니다."));
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
            .body("message", equalTo("questionChangeStatus.id: 게시글 ID는 양수이어야 합니다."));
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
}
