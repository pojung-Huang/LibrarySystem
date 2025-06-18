package tw.ispan.librarysystem.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.ispan.librarysystem.entity.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByIdNumber(String idNumber); // 用來檢查重複身分證
}

