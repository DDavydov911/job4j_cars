package ru.job4j.cars.store;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Ads;
import ru.job4j.cars.model.User;
import java.util.Optional;

@Repository
public class UserStoreDB implements RepoTrans {

    private final SessionFactory sf;

    public UserStoreDB(SessionFactory sf) {
        this.sf = sf;
    }

    public Optional<User> add(User user) {
        return Optional.ofNullable(
                this.tx(
                        session -> {
                            session.save(user);
                            return user;
                        }, sf)
        );
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        return this.tx(
                session -> session.createQuery(
                                "from ru.job4j.cars.model.User "
                                        + "where email = :email AND password = :pass"
                        )
                        .setParameter("email", email)
                        .setParameter("pass", password)
                        .uniqueResultOptional(),
                sf
        );
    }

    public Optional<User> getUserById(int id) {
        return this.tx(
                session -> session.createQuery(
                                "select distinct u from User u "
                                        + "left join fetch u.ads a "
                                        + "left join fetch a.car c "
                                        + "left join fetch c.engine "
                                        + "where u.id =: ID ", User.class
                        )
                        .setParameter("ID", id)
                        .uniqueResultOptional(),
                sf
        );
    }

    public User update(User user) {
        return this.tx(
                session -> {
                    User u = session.get(User.class, user.getId());
                    if (!user.getAds().isEmpty()) {
                        Ads ad = session.get(Ads.class,
                                user.getAds().get(user.getAds().size() - 1).getId());
                        ad.setUser(u);
                        u.addAdv(ad);
                        session.update(ad);
                    }
                    session.update(u);
                    return u;
                },
                sf
        );
    }
}
