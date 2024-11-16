package com.crud_library.core.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum CrudError {
	
	ENTITY_NOT_FOUND ("crud-library.entity.notfound", HttpStatus.NOT_FOUND), 
	INVALID_PARAMS("crud-library.generic.invalidParams", HttpStatus.BAD_REQUEST); 
	
	private String code;
	private HttpStatus status;
	
	private CrudError(String code, HttpStatus status) {
		this.code = code;
		this.status = status;
	}

}
