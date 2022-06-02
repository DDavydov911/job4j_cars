package ru.job4j.cars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Car;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarsRun {

    public static void main(String[] args) {
        List<Car> lastDayCars = new ArrayList<>();
        List<Car> carsWithPhotos = new ArrayList<>();
        List<Car> audiCars = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
/**
 *  объявления за последний день
 */
            Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
            lastDayCars = session.createQuery(
                    "select distinct c from Car c "
                            + "join fetch c.engine "
                            + "left join fetch c.photos p "
                            + "where c.created >= :day", Car.class
            )
                    .setParameter("day", yesterday)
                    .list();

/**
 *  объявления только с фото
 */
            carsWithPhotos = session.createQuery(
                            "select distinct c from Car c "
                                    + "join fetch c.engine "
                                    + "join fetch c.carMark "
                                    + "join fetch c.photos p ", Car.class
                    )
                    .list();
 /**
 *  только Audi
 */
            audiCars = session.createQuery(
                            "select distinct c from Car c "
                                    + "join fetch c.engine "
                                    + "join fetch c.carMark m "
                                    + "left join c.photos p "
                                    + "where m.name = :mark", Car.class
                    )
                    .setParameter("mark", "Audi")
                    .list();

            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        System.out.println("Only for last day: " + lastDayCars.size());
        lastDayCars.forEach(System.out::println);
        System.out.println("Only with photo: " + carsWithPhotos.size());
        carsWithPhotos.forEach(System.out::println);
        System.out.println("Only Audi: " + audiCars.size());
        audiCars.forEach(System.out::println);
    }
}
