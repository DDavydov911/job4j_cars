package ru.job4j.cars.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.function.Function;

@Repository
public class CarsRepositoryDB {
    private final SessionFactory sf;

    public CarsRepositoryDB(SessionFactory sf) {
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

    public Car saveOrUpdateCar(Car car) {
        return this.tx(session -> {
            session.saveOrUpdate(car);
            return car;
        });
    }

    public boolean deleteCar(Car car) {
        return this.tx(session -> {
            session.remove(car);
            return true;
        });
    }

    public Car findCarById(int carId) {
        return this.tx(
                session -> session.createQuery(
                        "select distinct c from Car c "
                        + "left join fetch c.engine "
                        + "left join fetch c.carMark "
                        + "left join fetch c.photos "
                        + "where c.id =: ID", Car.class
                )
                .setParameter("ID", carId)
                .uniqueResult()
        );
    }
}
