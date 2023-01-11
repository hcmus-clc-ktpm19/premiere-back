package org.hcmus.premiere.repository.custom;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class PremiereAbstractCustomRepository<T> {

  @PersistenceContext
  protected EntityManager entityManager;

  public JPAQuery<T> select(Expression<T> select) {
    return new JPAQuery<T>(entityManager).select(select);
  }

  public JPAQuery<T> selectFrom(EntityPath<?>... entity) {
    return new JPAQuery<T>(entityManager).from(entity);
  }
}
