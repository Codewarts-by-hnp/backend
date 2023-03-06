package com.codewarts.noriter.wish.controller;

import com.codewarts.noriter.auth.LoginCheck;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import com.codewarts.noriter.wish.service.WishService;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        @Valid @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long> map,
        @LoginCheck Long memberId) {
        if (!map.containsKey("articleId")) {
            throw new GlobalNoriterException(CommonExceptionType.INVALID_REQUEST);
        }
        wishService.create(memberId, map.get("articleId"));
    }

    @DeleteMapping
    public void remove(@RequestBody Map<String,
        @Valid @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long> map,
        @LoginCheck Long memberId) {
        if (!map.containsKey("articleId")) {
            throw new GlobalNoriterException(CommonExceptionType.INVALID_REQUEST);
        }
        wishService.delete(memberId, map.get("articleId"));
    }
}
