package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

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
            member.setUsername("member1");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(30);
            member2.setType(MemberType.ADMIN);

            member.setTeam(team);


            em.persist(member);
            em.persist(member2);

            em.flush();
            em.clear();

            String teamName = "teamA";
            String query = "select m, t from Member m left join Team t on m.username=t.name";
            List<Object[]> members = em.createQuery(query)
                    .getResultList();
            for (Object[] objects : members) {
                try {
                    Member m = (Member) objects[0];
                    Team t = (Team) objects[1];
                    System.out.println("m.getUsername() = " + m.getUsername());
                    System.out.println("t.getName() = " + t.getName());
                } catch (NullPointerException e) {
                    System.out.println("null");
                }
            }

            System.out.println("members.size() = " + members.size());


            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }
}
