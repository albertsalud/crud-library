package com.crud_library.core.mappers;

import java.util.List;

public interface CrudMapper<D, B> {
	
	D mapToDomain(B bean);
	B mapToBean(D domain);
	List<D> mapToDomainList(List<B> beansList);
	List<B> mapToBeanList(List<D> domainList);

}
