package com.crud_library.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	public ResponseEntity<CrudErrorResponse> manageMethodNotAllowedException(MethodNotAllowedException exception) {
		return ResponseEntity
				.status(exception.getStatusCode())
				.body(
					CrudErrorResponse.builder()
						.message(exception.getMessage())
						.build()
						);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CrudErrorResponse> manageMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		
		return ResponseEntity
				.status(exception.getStatusCode())
				.body(
					CrudErrorResponse.builder()
						.message(getDefaultMessages(exception.getFieldErrors()))
						.build()
						);
	}

	private String getDefaultMessages(List<FieldError> fieldErrors) {
		StringBuilder sb = new StringBuilder();
		fieldErrors.forEach(fe -> {
			sb.append(fe.getField() + ": " + fe.getDefaultMessage() + " - ");
		});
		sb.append("END");
		return sb.toString();
	}
}
