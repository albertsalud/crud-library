package com.crud_library.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CrudRepository<E, I> extends JpaRepository<E, I>, QuerydslPredicateExecutor<E>{

}
