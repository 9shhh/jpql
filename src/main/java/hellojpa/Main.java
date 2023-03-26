package hellojpa;

import hellojpa.jpql.Member;

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

            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class); // => TypedQuery<T>
            String singleResult1 = em.createQuery("select m.username from Member m where m.username = :username", String.class) // 리턴 타입이 명확함. String => TypedQuery<T>
                    .setParameter("username", "member1")
                    .getSingleResult();
            Query query2 = em.createQuery("select m.username, m.age from Member m");// 리턴 타입이 명확하지 않음. username = String, age = Integer => Query<T>

            // 여러개
            List<Member> resultList = query.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            // 1개
            // 표준 스펙에는 결과가 없으면, javax.persistence.NoResultException
            // 결과가 둘 이상이면, javax.persistence.NonUniqueResultException
            // Spring Data JPA -> Spring 이 해당 예외를 try/catch 해줌. NoResultException 발생하면 null or Optional 을 반환해줌.
            Member singleResult = query.getSingleResult();
            System.out.println("singleResult = " + singleResult);

            System.out.println("singleResult1 = " + singleResult1);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}