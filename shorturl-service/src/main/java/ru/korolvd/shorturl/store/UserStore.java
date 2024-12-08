package ru.korolvd.shorturl.store;

import ru.korolvd.shorturl.model.User;

import java.util.List;

public interface UserStore {

    void add(User user);

    User getById(String uuid);

    List<User> getAll();
}
