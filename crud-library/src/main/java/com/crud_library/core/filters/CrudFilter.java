package com.crud_library.core.filters;

import com.querydsl.core.BooleanBuilder;

public interface CrudFilter {
	
	public default BooleanBuilder getBuilder() {
		return new BooleanBuilder();
	}

}
