package ru.job4j.cars.service;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.User;
import ru.job4j.cars.store.UserStoreDB;
import ru.job4j.cars.store.UserStoreMem;

import java.util.Optional;

@Service
public class UserService {
    private final UserStoreMem userStore;

    public UserService(UserStoreMem userStore) {
        this.userStore = userStore;
    }

    public Optional<User> add(User user) {
        return userStore.add(user);
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        return userStore.findUserByEmailAndPassword(email, password);
    }
}
