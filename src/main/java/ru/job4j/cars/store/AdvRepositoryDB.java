package ru.job4j.cars.store;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.*;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Repository
public class AdvRepositoryDB {
    private final SessionFactory sf;

    public AdvRepositoryDB(SessionFactory sf) {
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

    public Ads add(Ads ad) {
        return this.tx(
                session -> {
                    Car car = ad.getCar();
                    CarMark mark = car.getCarMark();
                    Engine engine = car.getEngine();
                    if (!car.getPhotos().isEmpty()) {
                        List<Photo> photos = car.getPhotos();
                        photos.forEach(session::save);
                    }
                    session.save(engine);
                    session.saveOrUpdate(mark);
                    session.save(car);
                    session.save(ad);
                    return ad;
                }
        );
    }

    public List<Ads> findAll() {
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "join fetch a.car c "
                                        + "left join fetch c.engine "
                                        + "left join fetch c.photos p "
                                        + "where a.sold = false "
                                        + "order by a.id", Ads.class
                        )
                        .list()
        );
    }

    public Ads findAdvById(int adsId) {
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "join fetch a.car c "
                                        + "left join fetch c.engine "
                                        + "left join fetch c.photos p "
                                        + "where a.id =: id", Ads.class
                        )
                        .setParameter("id", adsId)
                        .uniqueResult()
        );
    }

    public List<Ads> getLastDayAds() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "join fetch a.car c "
                                        + "left join fetch c.engine "
                                        + "left join fetch c.photos p "
                                        + "where a.created >= :day and a.sold = false "
                                        + "order by a.id", Ads.class
                        )
                        .setParameter("day", yesterday)
                        .list()
        );
    }

    public List<Ads> getAdsWithPhoto() {
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "join fetch a.car c "
                                        + "join fetch c.carMark "
                                        + "left join fetch c.engine "
                                        + "left join fetch c.photos p "
                                        + "where p.photo is not empty and a.sold = false "
                                        + "order by a.id", Ads.class
                        )
                        .list()
        );
    }

    public List<Ads> getAdsByMark(String carMark) {
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "join fetch a.car c "
                                        + "join fetch c.carMark m "
                                        + "left join fetch c.engine "
                                        + "left join c.photos p "
                                        + "where m.name = :mark and "
                                        + "a.sold = false", Ads.class
                        )
                        .setParameter("mark", carMark)
                        .list()
        );
    }

    public Ads updateAdv(Ads adv) {
        return this.tx(
                session -> {
                    session.update(adv.getCar());
                    session.update(adv);
                    return adv;
                }
        );
    }
}
