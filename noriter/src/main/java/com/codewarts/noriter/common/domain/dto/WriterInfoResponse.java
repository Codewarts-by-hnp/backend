package com.codewarts.noriter.common.domain.dto;

import com.codewarts.noriter.common.domain.Member;
import lombok.Getter;

@Getter
public class WriterInfoResponse {

    private final Long id;
    private final String nickName;
    private final String profileImage;

    public WriterInfoResponse(Member writer) {
        this.id = writer.getId();
        this.nickName = writer.getNickname();
        this.profileImage = writer.getProfileImageUrl();
    }

    public static WriterInfoResponse from(Member member) {
        return new WriterInfoResponse(member);
    }
}
