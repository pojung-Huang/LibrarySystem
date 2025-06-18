package tw.ispan.librarysystem.service.member;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tw.ispan.librarysystem.dto.member.MemberRegisterDto;
import tw.ispan.librarysystem.entity.member.Member;
import tw.ispan.librarysystem.repository.member.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepo;

    public void register(MemberRegisterDto dto) {
        if (memberRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("該 Email 已註冊");//檢查 email 是否重複
        }

        if (memberRepo.existsByIdNumber(dto.getIdNumber())) {
            throw new RuntimeException("該身分證已申請過"); // 檢查身分證是否重複
        }

        Member member = new Member();
        BeanUtils.copyProperties(dto, member);

        // 密碼加密
        member.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));

        memberRepo.save(member);
    }
}

