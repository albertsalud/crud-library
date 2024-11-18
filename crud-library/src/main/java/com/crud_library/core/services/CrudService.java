package com.crud_library.core.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crud_library.core.exceptions.CrudError;
import com.crud_library.core.exceptions.CrudException;
import com.crud_library.core.filters.CrudFilter;
import com.crud_library.core.repositories.CrudRepository;
import com.crud_library.utils.id_seeker.IdSeeker;
import com.crud_library.utils.id_seeker.IdSeekerByField;
import com.crud_library.utils.id_seeker.IdSeekerByMethod;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class CrudService<E, I> {
	
	protected final CrudRepository<E, I> repository;
	
	protected CrudService(CrudRepository<E, I> repository) {
		this.repository = repository;
	}
	
	public Optional<E> findById(I id) {
		if(id == null) {
			log.error("Trying to find entity with null parameter");
			throw new CrudException(CrudError.INVALID_PARAMS);
		}
		
		return repository.findById(id);
	}
	
	public List<E> findByFilter(CrudFilter filter) {
		if(filter == null) filter = new CrudFilter() {};
		
		return (List<E>) this.repository.findAll(filter.getBuilder());
	}
	
	public E create(E entity) {
		if(entity == null) {
			log.error("Trying to create null object");
			throw new CrudException(CrudError.INVALID_PARAMS);
		}
		return this.repository.save(entity);
	}
	
	@Transactional
	public E update(E entity) {
		if(entity == null) {
			log.error("Trying to update null object");
			throw new CrudException(CrudError.INVALID_PARAMS);
		}
		
		Object id = getEntityId(entity);
		this.findById((I) id).orElseThrow(() -> new CrudException(CrudError.ENTITY_NOT_FOUND, id));
		
		return this.repository.save(entity);
	}
	
	private Object getEntityId(E entity) {
		IdSeeker idSeeker = IdSeeker.set(new IdSeekerByField(), new IdSeekerByMethod());
			
		return idSeeker.getId(entity);
	}

	public E delete(I id) {
		E entityToReturn = this.findById(id).orElseThrow(() -> new CrudException(CrudError.ENTITY_NOT_FOUND, id));
		this.repository.delete(entityToReturn);
		
		return entityToReturn;
		
	}

	public Page<E> findByFilter(Pageable pageable, CrudFilter filter) {
		if(filter == null) filter = new CrudFilter() {};
		
		return this.repository.findAll(filter.getBuilder(), pageable);
	}

}
