package com.codewarts.noriter.article.controller;

import com.codewarts.noriter.article.dto.gathering.GatheringDetailResponse;
import com.codewarts.noriter.article.dto.gathering.GatheringUpdateRequest;
import com.codewarts.noriter.article.dto.gathering.GatheringListResponse;
import com.codewarts.noriter.article.dto.gathering.GatheringCreateRequest;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.service.GatheringService;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/gathering")
@Validated
public class GatheringController {

    private final GatheringService gatheringService;
    private final ConversionService conversionService;

    @PostMapping
    public Long register(@RequestBody @Valid GatheringCreateRequest gatheringCreateRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        return gatheringService.create(gatheringCreateRequest, memberId);
    }

    @GetMapping
    public List<GatheringListResponse> getList(
        @RequestParam(required = false) Map<String, String> paramMap, HttpServletRequest request) {
        Long memberId = getMemberId(request);

        if (paramMap.isEmpty()) {
            return gatheringService.findList(null, memberId);
        }
        if (paramMap.size() != 1 || !paramMap.containsKey("status")) {
            throw new GlobalNoriterException(CommonExceptionType.INCORRECT_REQUEST_PARAM);
        }

        StatusType status = conversionService.convert(paramMap.get("status"), StatusType.class);
        return gatheringService.findList(status, memberId);
    }

    @GetMapping("/{id}")
    public GatheringDetailResponse getDetail(@PathVariable(required = false)
    @NotNull(message = "ID가 비어있습니다.")
    @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id, HttpServletRequest request) {
        Long memberId = getMemberId(request);
        return gatheringService.findDetail(id, memberId);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable(required = false)
    @NotNull(message = "ID가 비어있습니다.")
    @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id, HttpServletRequest request) {
        Long memberId = getMemberId(request);
        gatheringService.delete(id, memberId);
    }

    @PatchMapping("/{id}")
    public void changeStatus(@PathVariable(required = false)
    @NotNull(message = "ID가 비어있습니다.")
    @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        @RequestBody Map<String, StatusType> map,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        gatheringService.updateCompletion(id, memberId, map.get("status"));
    }

    @PutMapping("/{id}")
    public void edit(@PathVariable(required = false)
    @NotNull(message = "ID가 비어있습니다.")
    @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        @RequestBody @Valid GatheringUpdateRequest gatheringUpdateRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        gatheringService.update(id, gatheringUpdateRequest, memberId);
    }

    private Long getMemberId(HttpServletRequest request) {
        return (Long) request.getAttribute("memberId");
    }
}
