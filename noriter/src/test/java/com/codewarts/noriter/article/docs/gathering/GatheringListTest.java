package com.codewarts.noriter.article.docs.gathering;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("스터디게시판 전체 조회 기능 통합 테스트")
class GatheringListTest extends InitIntegrationRestDocsTest {

    @Test
    void 리스트를_조회한다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .param("status", "INCOMPLETE")

        .when()
            .get("/community/gathering")

        .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].id", equalTo(1))
            .body("[0].title", equalTo("테스트를 해볼것이당"))
            .body("[0].content", equalTo("안녕하냐고오옹"))
            .body("[0].writerNickname", equalTo("admin1"))
            .body("[0].sameWriter", equalTo(false))
            .body("[0].createdTime", equalTo("2022-11-11 16:25:58"))
            .body("[0].lastModifiedTime", equalTo("2022-11-11 16:25:58"))
            .body("[0].hashtags[0]", equalTo("SPRING"))
            .body("[0].hashtags[1]", equalTo("JPA"))
            .body("[0].wish", equalTo(false))
            .body("[0].wishCount", equalTo(1))
            .body("[0].commentCount", equalTo(4));
    }
    @Test
    void 로그인_후_리스트를_조회한다() {
        String accessToken = jwtProvider.issueAccessToken(2L);

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("status", "INCOMPLETE")

        .when()
            .get("/community/gathering")

        .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].id", equalTo(1))
            .body("[0].title", equalTo("테스트를 해볼것이당"))
            .body("[0].content", equalTo("안녕하냐고오옹"))
            .body("[0].writerNickname", equalTo("admin1"))
            .body("[0].sameWriter", equalTo(false))
            .body("[0].createdTime", equalTo("2022-11-11 16:25:58"))
            .body("[0].lastModifiedTime", equalTo("2022-11-11 16:25:58"))
            .body("[0].hashtags[0]", equalTo("SPRING"))
            .body("[0].hashtags[1]", equalTo("JPA"))
            .body("[0].wish", equalTo(true))
            .body("[0].wishCount", equalTo(1))
            .body("[0].commentCount", equalTo(4));
    }

    @Test
    void 잘못된_requestParam_값인_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .param("comp", "INCOMPLETE")

        .when()
            .get("/community/gathering")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INCORRECT_REQUEST_PARAM.getErrorCode()))
            .body("message", equalTo(CommonExceptionType.INCORRECT_REQUEST_PARAM.getErrorMessage()));
    }
    @Test
    void 잘못된_requestParam_타입인_경우_예외를_발생시킨다() {

        given(documentationSpec)
            .contentType(APPLICATION_JSON_VALUE)
            .param("status", "dd")

        .when()
            .get("/community/gathering")

        .then()
            .statusCode(CommonExceptionType.INVALID_REQUEST.getStatus().value())
            .body("errorCode", equalTo(CommonExceptionType.INCORRECT_REQUEST_VALUE.getErrorCode()))
            .body("message", equalTo(CommonExceptionType.INCORRECT_REQUEST_VALUE.getErrorMessage()));
    }

}
