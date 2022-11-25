package com.codewarts.noriter.article.docs.study;

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
public class StudyDetailTest {

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
            .pathParam("id", 1)

            .when()
            .get("/community/gathering/{id}")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(1))
            .body("title", equalTo("테스트를 해볼것이당"))
            .body("content", equalTo("안녕하냐고오옹"))
            .body("writer.id", equalTo(1))
            .body("writer.nickname", equalTo("admin1"))
            .body("writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/111111?v=4"))
            .body("hashtag[0]", equalTo("SPRING"))
            .body("hashtag[1]", equalTo("JPA"))
            .body("hashtag[2]", equalTo("난자유야"))
            .body("writtenTime", equalTo("2022-11-11T16:25:58.991061"))
            .body("editedTime", equalTo("2022-11-11T16:25:58.991061"))
            .body("wishCount", equalTo(0))
            .body("completed", equalTo(false))
            .body("comment[0].id", equalTo(1))
            .body("comment[0].content", equalTo("우왕 잘봤어용"))
            .body("comment[0].writer.id", equalTo(2))
            .body("comment[0].writer.nickname", equalTo("admin2"))
            .body("comment[0].writer.profileImage", equalTo("https://avatars.githubusercontent.com/u/222222?v=4"));
    }

}
