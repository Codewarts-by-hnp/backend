package com.codewarts.noriter.article.docs.free;

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
class FreeDetailTest {

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
            .pathParam("id", 10)

            .when()
            .get("/community/playground/{id}")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(10))
            .body("title", equalTo("붕어빵 먹고싶어요"))
            .body("content", equalTo("강남 붕어빵 맛잇는 집"))
            .body("writer.id", equalTo(1))
            .body("writer.nickname", equalTo("admin1"))
            .body("writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/111111?v=4"))
            .body("sameWriter", equalTo(false))
            .body("hashtags[0]", equalTo("강남역"))
            .body("hashtags[1]", equalTo("붕어팥"))
            .body("wish", equalTo(false))
            .body("wishCount", equalTo(1))
            .body("comment[0].id", equalTo(5))
            .body("comment[0].content", equalTo("강남역 11번출구에서 팔아요"))
            .body("comment[0].writer.id", equalTo(2))
            .body("comment[0].writer.nickname", equalTo("admin2"))
            .body("comment[0].writer.profileImage", equalTo("https://avatars.githubusercontent.com/u/222222?v=4"));
    }

    @Test
    void 로그인_후_상세조회를_한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 10)

            .when()
            .get("/community/playground/{id}")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(10))
            .body("title", equalTo("붕어빵 먹고싶어요"))
            .body("content", equalTo("강남 붕어빵 맛잇는 집"))
            .body("writer.id", equalTo(1))
            .body("writer.nickname", equalTo("admin1"))
            .body("writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/111111?v=4"))
            .body("sameWriter", equalTo(false))
            .body("hashtags[0]", equalTo("강남역"))
            .body("hashtags[1]", equalTo("붕어팥"))
            .body("wish", equalTo(true))
            .body("wishCount", equalTo(1))
            .body("comment[0].id", equalTo(5))
            .body("comment[0].content", equalTo("강남역 11번출구에서 팔아요"))
            .body("comment[0].writer.id", equalTo(2))
            .body("comment[0].writer.nickname", equalTo("admin2"))
            .body("comment[0].writer.profileImage", equalTo("https://avatars.githubusercontent.com/u/222222?v=4"));
    }

    @Test
    void id가_유효하지_않는_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", -1)

            .when()
            .get("/community/playground/{id}")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("freeDetail.id: 게시글 ID는 양수이어야 합니다."));
    }

    @Test
    void Path_Variable이_없는_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", " ")

            .when()
            .get("/community/playground/{id}")

            .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INVALID_REQUEST.getErrorCode()))
            .body("message", equalTo("freeDetail.id: ID가 비어있습니다."));
    }

    @Test
    void id가_존재하지_않는_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("id", 999999999)

            .when()
            .get("/community/playground/{id}")

            .then()
            .statusCode(ArticleExceptionType.ARTICLE_NOT_FOUND.getStatus().value())
            .body("errorCode", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ArticleExceptionType.ARTICLE_NOT_FOUND.getErrorMessage()));
    }
}
