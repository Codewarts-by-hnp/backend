package com.codewarts.noriter.article.docs.question;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
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
import com.codewarts.noriter.exception.type.CommonExceptionType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
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
class QuestionDetailTest {

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
    void 상세조회를_한다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", 6)

            .when()
            .get("/community/question/{id}")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(6))
            .body("title", equalTo("질문1"))
            .body("content", equalTo("궁금1"))
            .body("writer.id", equalTo(1))
            .body("writer.nickname", equalTo("admin1"))
            .body("writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/111111?v=4"))
            .body("sameWriter", equalTo(false))
            .body("hashtag[0]", equalTo("스프링"))
            .body("hashtag[1]", equalTo("코린이"))
            .body("hashtag[2]", equalTo("도와줘요"));
    }

    @Test
    void id가_유효하지_않는_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", -1)

            .when()
            .get("/community/question/{id}")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("questionDetail.id: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void Path_Variable이_없는_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", " ")

            .when()
            .get("/community/question/{id}")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("questionDetail.id: ID가 비어있습니다."));
    }

    @Test
    void id가_존재하지_않는_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", 999999999)

            .when()
            .get("/community/question/{id}")

            .then()
            .statusCode(ArticleExceptionType.ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorMessage()));
    }
}
