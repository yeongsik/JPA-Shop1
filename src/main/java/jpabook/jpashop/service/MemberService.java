package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
// 읽기전용 트랜잭션은 리소스를 너무 많이 사용하지 않는다.
// 읽기에는 readOnly = true 넣는 것이 좋다.
//@AllArgsConstructor // 필드의 생성자를 만들어주는 어노테이션
@RequiredArgsConstructor // 파이널이 있는 필드만 가지고 생성자를 만들어준다.
public class MemberService {

    private final MemberRepository memberRepository;
    // 필드 인젝션
    // 단점 memberRepository를 못바꾼다. (Access 방법X)

//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    // Setter 인젝션 단점 : 실제 애플리케이션 돌아가는 시점에 누군가 바꿀 수 있다.

    // 생성자 인젝션 추천
    // 생성자가 하나만 있을 경우 Autiwired가 없어도 Spring boot가 해준다.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 회원 가입
    @Transactional
    public Long join(Member member) {

        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
            // 실무에서는 멀티 스레드를 고려해서 멤버의 네임을 유니크 제약조건으로 둔다.
        }
    }

    // 회원 전체 조회

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
