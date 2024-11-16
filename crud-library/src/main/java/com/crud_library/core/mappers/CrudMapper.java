package com.crud_library.core.mappers;

import java.util.List;

public interface CrudMapper<E, B> {
	
	E mapToEntity(B bean);
	B mapToBean(E entity);
	List<E> mapToEntities(List<B> beans);
	List<B> mapToBeans(List<E> entities);

}
