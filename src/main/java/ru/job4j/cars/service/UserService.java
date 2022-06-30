package ru.job4j.cars.service;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.User;
import ru.job4j.cars.store.UserStoreDB;

import java.util.Optional;

@Service
public class UserService {
    private final UserStoreDB userStoreDB;

    public UserService(UserStoreDB userStoreDB) {
        this.userStoreDB = userStoreDB;
    }

    public Optional<User> add(User user) {
        return userStoreDB.add(user);
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        return userStoreDB.findUserByEmailAndPassword(email, password);
    }

    public Optional<User> getUserById(int id) {
        return userStoreDB.getUserById(id);
    }

    public void update(User user1) {
        userStoreDB.update(user1);
    }
}
