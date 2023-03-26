package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(20);
            member.changeTeam(team);

            em.persist(member);

            String query = "select m from Member m join m.team t";
            em.createQuery(query, Member.class)
                    .getResultList();

            // 세타 조인(cross join)
            String query2 = "select m from Member m, Team t where m.username = t.name";
            em.createQuery(query2, Member.class)
                    .getResultList();

            // 조인 대상 필터링
            // 회원과 팀을 조인, 팀 이름이 teamA 인 팀만 조인
            // ...
            // and ( -> 이런식으로 조인 조건이 들어감
            //         team1_.name='teamA'
            // )
            String query3 = "select m from Member m left join m.team t on t.name = 'teamA'";
            em.createQuery(query3, Member.class)
                    .getResultList();

            // 연관관계 없는 엔티티 외부 조인
            // 회원의 이름과 팀의 이름이 같은 대상 외부 조인
            // Team team1_
            //      on (
            //         member0_.username=team1_.name
            //      )
            String query4 = "select m from Member m join Team t on m.username = t.name";
            em.createQuery(query4, Member.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}