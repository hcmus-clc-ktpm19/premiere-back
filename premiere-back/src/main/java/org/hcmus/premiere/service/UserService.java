package org.hcmus.premiere.service;

import org.hcmus.premiere.model.entity.User;

public interface UserService {
  User findUserById(Long id);

}
