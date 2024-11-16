package com.crud_library.core.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import com.crud_library.core.exceptions.CrudErrorResponse;
import com.crud_library.core.exceptions.CrudException;

@RestControllerAdvice
public class CrudControllerAdvice {
	
	private final MessageSource messageSource;
	
	public CrudControllerAdvice(@Qualifier("crudMessageSource") MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@ExceptionHandler(CrudException.class)
	public ResponseEntity<CrudErrorResponse> manageCrudException(CrudException exception) {
		return ResponseEntity
				.status(exception.getError().getStatus().value())
				.body(
					CrudErrorResponse.builder()
						.message(messageSource.getMessage(exception.getError().getCode(), 
								exception.getArguments(),
								LocaleContextHolder.getLocale()))
						.build()
						);
	}
	
	@ExceptionHandler(MethodNotAllowedException.class)
	public ResponseEntity<CrudErrorResponse> manageCrudException(MethodNotAllowedException exception) {
		return ResponseEntity
				.status(exception.getStatusCode())
				.body(
					CrudErrorResponse.builder()
						.message(exception.getMessage())
						.build()
						);
	}
}
