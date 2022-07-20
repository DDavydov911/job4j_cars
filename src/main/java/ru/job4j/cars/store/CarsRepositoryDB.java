package ru.job4j.cars.store;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

@Repository
public class CarsRepositoryDB implements RepoTrans {
    private final SessionFactory sf;

    public CarsRepositoryDB(SessionFactory sf) {
        this.sf = sf;
    }

    public Car saveOrUpdateCar(Car car) {
        return this.tx(session -> {
            session.saveOrUpdate(car);
            return car;
        }, sf);
    }

    public boolean deleteCar(Car car) {
        return this.tx(session -> {
            session.remove(car);
            return true;
        }, sf);
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
                .uniqueResult(),
                sf
        );
    }
}
