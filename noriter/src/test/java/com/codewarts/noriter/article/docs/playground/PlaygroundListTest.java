package com.codewarts.noriter.article.docs.playground;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("자유게시판 전체 조회 기능 통합 테스트")
class PlaygroundListTest extends InitIntegrationRestDocsTest {

    @Test
    void 리스트를_조회한다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)

        .when()
            .get("/community/playground")

        .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].id", equalTo(10))
            .body("[0].title", equalTo("붕어빵 먹고싶어요"))
            .body("[0].content", equalTo("강남 붕어빵 맛잇는 집"))
            .body("[0].writerNickname", equalTo("admin2"))
            .body("[0].sameWriter", equalTo(false))
            .body("[0].createdTime", equalTo("2022-11-25 16:25:58"))
            .body("[0].lastModifiedTime", equalTo("2022-11-25 16:25:58"))
            .body("[0].hashtags[0]", equalTo("강남역"))
            .body("[0].hashtags[1]", equalTo("붕어팥"))
            .body("[0].wish", equalTo(false))
            .body("[0].wishCount", equalTo(1))
            .body("[0].commentCount", equalTo(2));
    }

    @Test
    void 로그인_후_리스트를_조회한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)

        .when()
            .get("/community/playground")

        .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].id", equalTo(10))
            .body("[0].title", equalTo("붕어빵 먹고싶어요"))
            .body("[0].content", equalTo("강남 붕어빵 맛잇는 집"))
            .body("[0].writerNickname", equalTo("admin2"))
            .body("[0].sameWriter", equalTo(true))
            .body("[0].createdTime", equalTo("2022-11-25 16:25:58"))
            .body("[0].lastModifiedTime", equalTo("2022-11-25 16:25:58"))
            .body("[0].hashtags[0]", equalTo("강남역"))
            .body("[0].hashtags[1]", equalTo("붕어팥"))
            .body("[0].wish", equalTo(true))
            .body("[0].wishCount", equalTo(1))
            .body("[0].commentCount", equalTo(2));
    }
}
