package com.codewarts.noriter.member.repository;

import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.auth.oauth.type.ResourceServer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByResourceServerAndResourceServerId(ResourceServer resourceServer, Long resourceServerId);
}
