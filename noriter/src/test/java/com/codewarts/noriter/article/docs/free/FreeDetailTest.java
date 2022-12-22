package com.codewarts.noriter.article.docs.free;

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
public class FreeDetailTest {

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
            .body("hashtag[0]", equalTo("강남역"))
            .body("hashtag[1]", equalTo("붕어팥"))
            .body("writtenTime", equalTo("2022-11-25T16:25:58.991061"))
            .body("editedTime", equalTo("2022-11-25T16:25:58.991061"))
            .body("wishCount", equalTo(0))
            .body("comment[0].id", equalTo(5))
            .body("comment[0].content", equalTo("강남역 11번출구에서 팔아요"))
            .body("comment[0].writer.id", equalTo(2))
            .body("comment[0].writer.nickname", equalTo("admin2"))
            .body("comment[0].writer.profileImage", equalTo("https://avatars.githubusercontent.com/u/222222?v=4"));
    }

}
