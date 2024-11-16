package com.crud_library.core.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.crud_library.core.exceptions.CrudError;
import com.crud_library.core.exceptions.CrudException;
import com.crud_library.core.filters.CrudFilter;
import com.crud_library.core.mappers.CrudMapper;
import com.crud_library.core.services.CrudService;

import jakarta.validation.Valid;

public abstract class CrudController<E, B, I> {
	
	private final CrudService<E, I> service;
	private final CrudMapper<E, B> mapper;
	
	protected CrudController (CrudService<E, I> service,
			CrudMapper<E, B> mapper) {
		this.service = service;
		this.mapper = mapper;
	}
	
	@GetMapping("/{id}")
	public B findById(@PathVariable I id) {
		return mapper.mapToBean(
				service.findById(id).orElseThrow(() -> new CrudException(CrudError.ENTITY_NOT_FOUND, id)));
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public B create(@Valid @RequestBody B bean) {
		return mapper.mapToBean(
				service.create(
						mapper.mapToEntity(bean))
				);
	}
	
	@PutMapping
	public B update(@Valid @RequestBody B bean) {
		return mapper.mapToBean(
				service.update(mapper.mapToEntity(bean))
				);
	}
	
	@DeleteMapping("/{id}")
	public B deleteById(@PathVariable I id) {
		return mapper.mapToBean(
				service.delete(id));
	}
	
	
	@GetMapping
	public List<B> findByFilter(CrudFilter filter) {
		return this.mapper.mapToBeans(
				service.findByFilter(filter)
				);
	}
	
	@GetMapping("/paginated")
	public Page<B> findByFilter(Pageable pageable, CrudFilter filter) {
		Page<E> serviceResponse = service.findByFilter(pageable, filter);
		
		return new PageImpl<B>(mapper.mapToBeans(serviceResponse.getContent()), pageable, serviceResponse.getTotalElements());
	}

}
