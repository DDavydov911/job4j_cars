package ru.job4j.cars.store;


import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.*;

import java.util.Date;
import java.util.List;

@Repository
public class AdvRepositoryDB implements RepoTrans {
    private final SessionFactory sf;

    public AdvRepositoryDB(SessionFactory sf) {
        this.sf = sf;
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
                    if (ad.getUser() != null) {
                        session.save(ad.getUser());
                    }
                    session.save(ad);
                    return ad;
                }, sf
        );
    }

    public List<Ads> findAll() {
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "left join fetch a.car c "
                                        + "left join fetch c.photos p "
                                        + "left join fetch c.engine "
                                        + "where a.sold = false "
                                        + "order by a.id desc", Ads.class
                        )
                        .list(),
                sf
        );
    }

    public Ads findAdvById(int adsId) {
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "left join fetch a.car c "
                                        + "left join fetch c.engine "
                                        + "left join fetch c.photos p "
                                        + "where a.id =: id", Ads.class
                        )
                        .setParameter("id", adsId)
                        .uniqueResult(),
                sf
        );
    }

    public List<Ads> getLastDayAds() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "left join fetch a.car c "
                                        + "left join fetch c.engine "
                                        + "left join fetch c.photos p "
                                        + "where a.created >= :day and a.sold = false "
                                        + "order by a.id desc ", Ads.class
                        )
                        .setParameter("day", yesterday)
                        .list(),
                sf
        );
    }

    public List<Ads> getAdsWithPhoto() {
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "left join fetch a.car c "
                                        + "left join fetch c.carMark "
                                        + "left join fetch c.engine "
                                        + "left join fetch c.photos p "
                                        + "where p.photo is not null and a.sold = false "
                                        + "order by a.id desc", Ads.class
                        )
                        .list(),
                sf
        );
    }

    public List<Ads> getAdsByMark(String carMark) {
        return this.tx(
                session -> session.createQuery(
                                "select distinct a from Ads a "
                                        + "left join fetch a.car c "
                                        + "left join fetch c.carMark m "
                                        + "left join fetch c.engine "
                                        + "left join c.photos p "
                                        + "where m.name = :mark and "
                                        + "a.sold = false order by a.id desc", Ads.class
                        )
                        .setParameter("mark", carMark)
                        .list(),
                sf
        );
    }

    public Ads updateAdv(Ads adv) {
        return this.tx(
                session -> {
                    session.update(adv.getCar());
                    session.update(adv);
                    return adv;
                }, sf
        );
    }

    public boolean deleteAdv(int id) {
        return Boolean.TRUE.equals(this.tx(
                session -> {
                    Ads ad = session.get(Ads.class, id);
                    session.remove(ad.getCar().getCarMark());
                    session.remove(ad.getCar().getEngine());
                    session.remove(ad.getCar());
                    session.remove(ad);
                    return true;
                }, sf
        ));
    }
}
