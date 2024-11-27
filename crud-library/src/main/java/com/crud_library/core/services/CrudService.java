package com.crud_library.core.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crud_library.core.exceptions.CrudError;
import com.crud_library.core.exceptions.CrudException;
import com.crud_library.core.repositories.CrudRepository;
import com.crud_library.utils.id_seeker.IdSeeker;
import com.crud_library.utils.id_seeker.IdSeekerByField;
import com.crud_library.utils.id_seeker.IdSeekerByMethod;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CrudService<D, I> {
	
	protected final CrudRepository<D, I> repository;
	
	protected CrudService(CrudRepository<D, I> repository) {
		this.repository = repository;
	}
	
	public Optional<D> findById(I id) {
		if(id == null) {
			log.error("Trying to find entity with null parameter");
			throw new CrudException(CrudError.INVALID_PARAMS);
		}
		
		return repository.findById(id);
	}
	
	public List<D> findAll() {
		return (List<D>) this.repository.findAll();
	}
	
	public D create(D domain) {
		if(domain == null) {
			log.error("Trying to create null object");
			throw new CrudException(CrudError.INVALID_PARAMS);
		}
		return this.repository.save(domain);
	}
	
	@Transactional
	public D update(D domain) {
		if(domain == null) {
			log.error("Trying to update null object");
			throw new CrudException(CrudError.INVALID_PARAMS);
		}
		
		Object id = getEntityId(domain);
		this.findById((I) id).orElseThrow(() -> new CrudException(CrudError.ENTITY_NOT_FOUND, id));
		
		return this.repository.save(domain);
	}
	
	private Object getEntityId(D domain) {
		IdSeeker idSeeker = IdSeeker.set(new IdSeekerByField(), new IdSeekerByMethod());
			
		return idSeeker.getId(domain);
	}

	public D delete(I id) {
		D domainToReturn = this.findById(id).orElseThrow(() -> new CrudException(CrudError.ENTITY_NOT_FOUND, id));
		this.repository.delete(domainToReturn);
		
		return domainToReturn;
		
	}

	public Page<D> findAll(Pageable pageable) {
		
		return this.repository.findAll(pageable);
	}

}
