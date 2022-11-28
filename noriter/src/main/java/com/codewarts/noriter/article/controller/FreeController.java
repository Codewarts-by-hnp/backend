package com.codewarts.noriter.article.controller;


import com.codewarts.noriter.article.domain.dto.free.FreeDetailResponse;
import com.codewarts.noriter.article.domain.dto.free.FreeEditRequest;
import com.codewarts.noriter.article.domain.dto.free.FreeListResponse;
import com.codewarts.noriter.article.domain.dto.free.FreePostRequest;
import com.codewarts.noriter.article.service.FreeService;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/community/free")
public class FreeController {

    private final FreeService freeService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public void register(@RequestBody FreePostRequest freePostRequest, HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        freeService.create(freePostRequest, memberId);
    }

    @GetMapping("/{id}")
    public FreeDetailResponse freeDetail(@PathVariable Long id) {
        return freeService.findDetail(id);
    }

    @GetMapping()
    public List<FreeListResponse> freeDetail() {
        return freeService.findList();
    }

    @PutMapping("/{id}")
    public void postEdit(@PathVariable Long id, @RequestBody FreeEditRequest freeEditRequest,
        HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        freeService.update(id, freeEditRequest, memberId);
    }

}
