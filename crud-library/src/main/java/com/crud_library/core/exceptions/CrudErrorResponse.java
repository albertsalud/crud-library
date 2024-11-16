package com.crud_library.core.exceptions;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CrudErrorResponse {
	
	private String message;
	
	@Builder.Default
	private LocalDateTime datetime = LocalDateTime.now();

}
