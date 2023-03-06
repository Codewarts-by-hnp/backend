package com.codewarts.noriter.article.docs.playground;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.codewarts.noriter.article.docs.InitIntegrationRestDocsTest;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("자유게시판 상세 조회 기능 통합 테스트")
class PlaygroundDetailTest extends InitIntegrationRestDocsTest {

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
            .body("writer.id", equalTo(2))
            .body("writer.nickname", equalTo("admin2"))
            .body("writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/222222?v=4"))
            .body("sameWriter", equalTo(false))
            .body("hashtags[0]", equalTo("강남역"))
            .body("hashtags[1]", equalTo("붕어팥"))
            .body("wish", equalTo(false))
            .body("wishCount", equalTo(1))
            .body("comment[0].id", equalTo(5))
            .body("comment[0].content", equalTo("강남역 11번출구에서 팔아요"))
            .body("comment[0].writer.id", equalTo(2))
            .body("comment[0].writer.nickname", equalTo("admin2"))
            .body("comment[0].writer.profileImage", equalTo("https://avatars.githubusercontent.com/u/222222?v=4"))
            .body("comment[0].recomments.size()", is(1))
            .body("comment[0].recomments[0].id", equalTo(6))
            .body("comment[0].recomments[0].writer.id", equalTo(2))
            .body("comment[0].recomments[0].content", equalTo("잘 봤다니 다행입니당"));
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
            .body("writer.id", equalTo(2))
            .body("writer.nickname", equalTo("admin2"))
            .body("writer.profileImage",
                equalTo("https://avatars.githubusercontent.com/u/222222?v=4"))
            .body("sameWriter", equalTo(true))
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
            .body("message", equalTo("getDetail.id: 게시글 ID는 양수이어야 합니다."));
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
            .body("message", equalTo("getDetail.id: ID가 비어있습니다."));
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
