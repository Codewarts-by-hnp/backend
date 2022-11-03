package com.codewarts.noriter.oauth;

import com.codewarts.noriter.MemberRepository;
import com.codewarts.noriter.domain.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member login(Member oauthUser) {
        Optional<Member> optionalMember = memberRepository.findByResourceServerAndResourceServerId(
            oauthUser.getResourceServer(),
            oauthUser.getResourceServerId());

        if (optionalMember.isEmpty()) {
            memberRepository.save(oauthUser);
            return oauthUser;
        }
        return optionalMember.get();
    }

    @Transactional
    public void updateRefreshToken(String jwtRefreshToken, Long memberId) {
        Member findMember = memberRepository.findById(memberId)
            .orElseThrow(RuntimeException::new);
        findMember.updateRefreshToken(jwtRefreshToken);
    }

    @Transactional
    public void deleteRefreshToken(Long decodedMemberId) {
        Member findMember = memberRepository.findById(decodedMemberId)
            .orElseThrow(RuntimeException::new);

        findMember.updateRefreshToken(null);
    }
}
