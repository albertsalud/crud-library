package com.crud_library.core.exceptions;

import lombok.Getter;

@Getter
public class CrudException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4143824598566983286L;
	
	private final CrudError error;
	private final Object[] arguments;
	
	public CrudException(CrudError error, Object... arguments) {
		this.error = error;
		this.arguments = arguments;
	}

}
