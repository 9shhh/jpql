package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.MemberDTO;

import javax.persistence.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            List<Member> result = em.createQuery("select m from Member m", Member.class) // select 절에 정의된 엔티티 모두 영속성 컨텍스트에서 관리됨.
                    .getResultList();
            Member findMember = result.get(0);
            findMember.setAge(20); // 따라서 해당 코드처럼 속성 값을 변경하면 정상적으로 데이터 반영됨.

            // 여러 값 조회
            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();

            Object[] resultGetFirst = resultList.get(0);
            System.out.println("username = " + resultGetFirst[0]);
            System.out.println("age = " + resultGetFirst[1]);

            // 여러 값 조회 - new 명령어로 조회
            // 단순 값을 DTO 로 바로 조회
            // MemberDTO 패키지 명을 다 적어줘야함. -> JPQL 이 문자열이라서...
            List<MemberDTO> resultList1 = em.createQuery("select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = resultList1.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}