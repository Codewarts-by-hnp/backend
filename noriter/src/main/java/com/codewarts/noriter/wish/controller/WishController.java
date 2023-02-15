package com.codewarts.noriter.wish.controller;

import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import com.codewarts.noriter.wish.service.WishService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/wish")
@Validated
public class WishController {

    private final WishService wishService;

    @PostMapping
    public void register(@RequestBody Map<String,
        @Valid @NotNull(message = "ID가 비어있습니다.") @Positive(message = "게시글 ID는 양수이어야 합니다.") Long> map,
        HttpServletRequest request) {
        if (!map.containsKey("articleId")) {
            throw new GlobalNoriterException(CommonExceptionType.INVALID_REQUEST);
        }
        Long memberId = getMemberId(request);
        wishService.create(memberId, map.get("articleId"));
    }

    private Long getMemberId(HttpServletRequest request) {
        return (Long) request.getAttribute("memberId");
    }
}
