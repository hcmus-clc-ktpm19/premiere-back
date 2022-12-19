package org.hcmus.premiere.repository;

import org.hcmus.premiere.model.entity.User;
import org.hcmus.premiere.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>,
    UserRepositoryCustom {
}