package ru.job4j.cars.persistence;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Repository
public class AdRepository {
    private final SessionFactory sf;

    public AdRepository(SessionFactory sf) {
        this.sf = sf;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public List<Car> getLastDayCars() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        return this.tx(
            session -> session.createQuery(
                "select distinct c from Car c "
                + "join fetch c.engine "
                + "left join fetch c.photos p "
                + "where c.created >= :day", Car.class
                )
                .setParameter("day", yesterday)
                .list()
        );
    }

    public List<Car> getCarsWithPhoto() {
        return this.tx(
            session -> session.createQuery(
                "select distinct c from Car c "
                + "join fetch c.engine "
                + "join fetch c.carMark "
                + "join fetch c.photos p ", Car.class
                )
                .list()
        );
    }

    public List<Car> getOnly(String carMark) {
        return this.tx(
            session -> session.createQuery(
                "select distinct c from Car c "
                + "join fetch c.engine "
                + "join fetch c.carMark m "
                + "left join c.photos p "
                + "where m.name = :mark", Car.class
                )
                .setParameter("mark", carMark)
                .list()
        );
    }
}
