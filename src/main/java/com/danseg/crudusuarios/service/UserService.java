package com.danseg.crudusuarios.service;

import com.danseg.crudusuarios.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    List<User> findUsers();

    User findUserById(Long userId);

    User updateUser(Long userId, User user);

    User updateEmail(Long userId, String email);

    Boolean deleteUser(Long userId);

    List<User> searchUsers(String name, String lastName);

    List<User> findUsersSortedByName();

    List<User> findUsersOver18();
}
