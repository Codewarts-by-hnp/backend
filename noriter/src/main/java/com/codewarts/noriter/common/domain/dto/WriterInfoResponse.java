package com.codewarts.noriter.common.domain.dto;

import com.codewarts.noriter.common.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WriterInfoResponse {

    private Long id;
    private String nickname;
    private String profileImage;

    public WriterInfoResponse(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImageUrl();
    }
}
