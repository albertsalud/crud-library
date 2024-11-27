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
import org.springframework.web.bind.annotation.RestController;

import com.crud_library.core.exceptions.CrudError;
import com.crud_library.core.exceptions.CrudException;
import com.crud_library.core.mappers.CrudMapper;
import com.crud_library.core.services.CrudService;

import jakarta.validation.Valid;

@RestController
public abstract class CrudController<D, B, I> {
	
	protected final CrudService<D, I> service;
	protected final CrudMapper<D, B> mapper;
	
	protected CrudController (CrudService<D, I> service,
			CrudMapper<D, B> mapper) {
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
						mapper.mapToDomain(bean))
				);
	}
	
	@PutMapping
	public B update(@Valid @RequestBody B bean) {
		return mapper.mapToBean(
				service.update(mapper.mapToDomain(bean))
				);
	}
	
	@DeleteMapping("/{id}")
	public B deleteById(@PathVariable I id) {
		return mapper.mapToBean(
				service.delete(id));
	}
	
	
	@GetMapping
	public List<B> findAll() {
		return this.mapper.mapToBeanList(
				service.findAll()
				);
	}
	
	@GetMapping("/paginated")
	public Page<B> findAll(Pageable pageable) {
		Page<D> serviceResponse = service.findAll(pageable);
		
		return new PageImpl<B>(mapper.mapToBeanList(serviceResponse.getContent()), pageable, serviceResponse.getTotalElements());
	}

}
