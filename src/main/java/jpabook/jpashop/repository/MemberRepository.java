package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;


    public void save(Member member) {
        em.persist(member);

    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m ", Member.class)
                .getResultList();
                // JPQL SQL 다름
                // JPQL 은 엔티티대상으로 한다.
                // JPQL 은 기본편에서 마스터
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name" , Member.class)
                .setParameter("name" , name)
                .getResultList();
    }

}
