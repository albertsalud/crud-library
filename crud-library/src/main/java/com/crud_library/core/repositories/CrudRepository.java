package com.crud_library.core.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudRepository<D, I> {
	
	Optional<D> findById(I id);
	Iterable<D> findAll();
	D save(D entity);
	void delete(D domain);
	Page<D> findAll(Pageable pageable);

}
