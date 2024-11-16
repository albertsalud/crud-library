package com.crud_library.core.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.crud_library.core.exceptions.CrudError;
import com.crud_library.core.exceptions.CrudException;
import com.crud_library.core.filters.CrudFilter;
import com.crud_library.core.mappers.CrudMapper;
import com.crud_library.core.services.CrudService;
import com.crud_library.samples.TestBean;
import com.crud_library.samples.TestEntity;

@ExtendWith(MockitoExtension.class)
public class CrudControllerTest {

	private CrudController<TestEntity, TestBean, Long> controller;
	
	@Mock
	private CrudService<TestEntity, Long> service;
	@Mock
	private CrudMapper<TestEntity, TestBean> mapper;
	
	@BeforeEach
	public void setup() {
		controller = Mockito.mock(CrudController.class, Mockito.withSettings()
				.useConstructor(service, mapper)
		        .defaultAnswer(Mockito.CALLS_REAL_METHODS));
	}
	
	@Test
	public void findById() {
		Long idToFind = new Random().nextLong();
		when(service.findById(anyLong())).thenReturn(Optional.of(createTestEntity(idToFind)));
		
		controller.findById(idToFind);
		verify(service).findById(eq(idToFind));
		verify(mapper).mapToBean(any());
		
	}
	
	@Test
	public void findById_whenEntityNotFoundThrowsException() {
		Long idToFind = new Random().nextLong();
		when(service.findById(anyLong())).thenReturn(Optional.empty());
		
		try {
			controller.findById(idToFind);
			fail("Exception expected!");
			
		} catch (Exception e) {
			assertTrue(e instanceof CrudException);
			assertEquals(CrudError.ENTITY_NOT_FOUND, ((CrudException)e).getError());
		}
		verify(service).findById(eq(idToFind));
		
	}
	
	@Test
	public void create() {
		TestBean testBean = createTestBean(null);
		TestEntity testEntity = createTestEntity(null);
		when(mapper.mapToEntity(any())).thenReturn(testEntity);
		when(mapper.mapToBean(any())).thenReturn(testBean);
		when(service.create(any())).thenReturn(testEntity);
		
		controller.create(testBean);
		verify(mapper).mapToBean(eq(testEntity));
		verify(mapper).mapToEntity(eq(testBean));
		verify(service).create(eq(testEntity));
	}
	
	@Test
	public void update() {
		TestBean testBean = createTestBean(null);
		TestEntity testEntity = createTestEntity(null);
		when(mapper.mapToEntity(any())).thenReturn(testEntity);
		when(service.update(any())).thenReturn(testEntity);
		
		controller.update(testBean);
		verify(mapper).mapToBean(eq(testEntity));
		verify(mapper).mapToEntity(eq(testBean));
		verify(service).update(eq(testEntity));
	}
	
	@Test
	public void deleteById() {
		Long idToDelete = new Random().nextLong();
		TestEntity testEntity = createTestEntity(idToDelete);
		when(service.delete(anyLong())).thenReturn(testEntity);
		
		controller.deleteById(idToDelete);
		verify(service).delete(eq(idToDelete));
		verify(mapper).mapToBean(eq(testEntity));
	}
	
	@Test
	public void findByFilter() {
		CrudFilter filter = new CrudFilter() {};
		
		when(service.findByFilter(any())).thenReturn(Collections.singletonList(createTestEntity(null)));
		controller.findByFilter(filter);
		
		verify(service).findByFilter(eq(filter));
		verify(mapper).mapToBeans(anyList());
	}
	
	@Test
	public void findByFilterPage()  {
		Pageable pageable = PageRequest.of(1, 10);
		CrudFilter filter = new CrudFilter() {};
		Page<TestEntity> page = new PageImpl<TestEntity>(Collections.emptyList(), pageable, 0);
		
		when(service.findByFilter(any(Pageable.class), any(CrudFilter.class))).thenReturn(page);
		Page<TestBean> response = controller.findByFilter(pageable, filter);
		
		assertNotNull(response);
		assertEquals(page.getTotalElements(), response.getTotalElements());
		verify(service).findByFilter(eq(pageable), eq(filter));
		verify(mapper).mapToBeans(eq(page.getContent()));
	}
	

	private TestBean createTestBean(Long id) {
		TestBean testBean = new TestBean();
		testBean.setCode("CODE");
		testBean.setId(id);
		testBean.setDescription("Description");
		
		return testBean;
	}

	private TestEntity createTestEntity(Long id) {
		TestEntity entity = new TestEntity();
		entity.setCode("CODE");
		entity.setId(id);
		entity.setDescription("Description");
		
		return entity;
	}
	
	
}
