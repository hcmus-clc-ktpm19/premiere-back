package org.hcmus.premiere.repository;

import java.util.List;
import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.repository.custom.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>,
    CustomUserRepository {

  List<User> findUsersByIdIn(List<Long> userIds);
}