package com.codewarts.noriter.common.domain.dto;

import com.codewarts.noriter.common.domain.Member;
import lombok.Getter;

@Getter
public class WriterInfoResponse {

    private final Long id;
    private final String nickname;
    private final String profileImage;

    public WriterInfoResponse(Member writer) {
        this.id = writer.getId();
        this.nickname = writer.getNickname();
        this.profileImage = writer.getProfileImageUrl();
    }

    public static WriterInfoResponse from(Member member) {
        return new WriterInfoResponse(member);
    }
}
