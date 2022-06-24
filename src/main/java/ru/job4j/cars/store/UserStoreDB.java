package ru.job4j.cars.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
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
}
