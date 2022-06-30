package ru.job4j.cars.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Ads;
import ru.job4j.cars.model.User;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class UserStoreDB {

    private final SessionFactory sf;

    public UserStoreDB(SessionFactory sf) {
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

    public Optional<User> add(User user) {
        return Optional.ofNullable(
                this.tx(
                        session -> {
                            session.save(user);
                            return user;
                        })
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
                        .uniqueResultOptional()
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
                        .uniqueResultOptional()
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
                }
        );
    }
}
