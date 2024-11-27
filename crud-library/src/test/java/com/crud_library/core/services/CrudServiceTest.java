package com.crud_library.core.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.crud_library.core.exceptions.CrudError;
import com.crud_library.core.exceptions.CrudException;
import com.crud_library.core.repositories.CrudRepository;
import com.crud_library.samples.TestEntity;

@ExtendWith(MockitoExtension.class)
public class CrudServiceTest {
	
	private CrudService<TestEntity, Long> service;
	
	@Mock
	private CrudRepository<TestEntity, Long> repository;
	
	@BeforeEach
	public void setUp() {
		service = Mockito.mock(CrudService.class, Mockito.withSettings().useConstructor(repository)
		        .defaultAnswer(Mockito.CALLS_REAL_METHODS));
		
	}
	
	@Test
	public void create() {
		TestEntity entityToSave = createTestEntity();
		when(repository.save(any())).thenReturn(entityToSave);
		
		service.create(entityToSave);
		verify(repository).save(eq(entityToSave));
	}
	
	@Test
	public void create_whenNullEntity_throwsException() {
		try {
			service.create(null);
			fail("Expected exception");
		} catch (Exception e) {
			assertTrue(e instanceof CrudException);
			assertEquals(CrudError.INVALID_PARAMS, ((CrudException)e).getError());
		}
		
		verify(repository, never()).save(any());

	}
	
	@Test
	public void findById() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(createTestEntity()));
		
		Optional<TestEntity> response = service.findById(1L);
		
		assertNotNull(response);
		assertNotNull(response.get());
		verify(repository).findById(eq(1L));
	}
	
	@Test
	public void findById_whenNullParam_throwsException() {
		try {
			service.findById(null);
			fail("Expected exception");
		} catch (Exception e) {
			assertTrue(e instanceof CrudException);
			assertEquals(CrudError.INVALID_PARAMS, ((CrudException)e).getError());
		}
		
		verify(repository, never()).findById(anyLong());
	}
	
	@Test
	public void findByFilter() {
		when(repository.findAll()).thenReturn(Collections.emptyList());
		
		service.findAll();
		verify(repository).findAll();
	}
	
	@Test
	public void findByFilter_whenFilterIsNull_returnsList() {
		when(repository.findAll()).thenReturn(Collections.emptyList());
		
		service.findAll(null);
		verify(repository).findAll();
	}
	
	@Test
	public void update() {
		TestEntity entityToUpdate = createTestEntity();
		entityToUpdate.setId(new Random().nextLong());
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(entityToUpdate));
		when(repository.save(any())).thenReturn(entityToUpdate);
		
		service.update(entityToUpdate);
		verify(repository).findById(eq(entityToUpdate.getId()));
		verify(repository).save(entityToUpdate);
	}
	
	@Test
	public void update_whenNullParams_throwsException() {
		try {
			service.update(null);
			fail("Expected exception");
		} catch (Exception e) {
			assertTrue(e instanceof CrudException);
			assertEquals(CrudError.INVALID_PARAMS, ((CrudException)e).getError());
		}
		
		verify(repository, never()).findById(anyLong());
		verify(repository, never()).save(any());
	}
	
	@Test
	public void update_whenNoId_throwsException() {
		try {
			service.update(createTestEntity());
			fail("Expected exception");
		} catch (Exception e) {
			assertTrue(e instanceof CrudException);
			assertEquals(CrudError.INVALID_PARAMS, ((CrudException)e).getError());
		}
		
		verify(repository, never()).findById(anyLong());
		verify(repository, never()).save(any());
	}
	
	@Test
	public void update_whenUnexistingId_throwsException() {
		TestEntity entityToUpdate = createTestEntity();
		entityToUpdate.setId(new Random().nextLong());
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		try {
			service.update(entityToUpdate);
			fail("Expected exception");
		} catch (Exception e) {
			assertTrue(e instanceof CrudException);
			assertEquals(CrudError.ENTITY_NOT_FOUND, ((CrudException)e).getError());
		}
		
		verify(repository).findById(eq(entityToUpdate.getId()));
		verify(repository, never()).save(any());
	}
	
	@Test
	public void delete() {
		Long idToDelete = new Random().nextLong();
		TestEntity entityToDelete = createTestEntity();
		entityToDelete.setId(idToDelete);
		
		when(repository.findById(idToDelete)).thenReturn(Optional.of(entityToDelete));
		TestEntity response = service.delete(idToDelete);
		assertNotNull(response);
		assertEquals(entityToDelete, response);
		
		verify(repository).findById(idToDelete);
		verify(repository).delete(entityToDelete);
		
	}
	
	@Test
	public void delete_whenNullParam_throwsException() {
		try {
			service.delete(null);
			fail("Expected exception");
		} catch (Exception e) {
			assertTrue(e instanceof CrudException);
			assertEquals(CrudError.INVALID_PARAMS, ((CrudException)e).getError());
		}
		
		verify(repository, never()).findById(anyLong());
		verify(repository, never()).delete(any());
		
	}
	
	@Test
	public void delete_whenUnexistingId_throwsException() {
		Long idToDelete = new Random().nextLong();
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		try {
			service.delete(idToDelete);
			fail("Expected exception");
		} catch (Exception e) {
			assertTrue(e instanceof CrudException);
			assertEquals(CrudError.ENTITY_NOT_FOUND, ((CrudException)e).getError());
		}
		
		verify(repository).findById(eq(idToDelete));
		verify(repository, never()).delete(any());
	}
	
	@Test
	public void findByFilterPageable() {
		Pageable pageable = PageRequest.of(0, 5);
		service.findAll(pageable);
		
		verify(repository).findAll(eq(pageable));
	}
	
	@Test
	public void findByFilterPageable_whenNullFilter_returnsResult() {
		Pageable pageable = PageRequest.of(0, 5);
		service.findAll(pageable);
		
		verify(repository).findAll(eq(pageable));
	}
	

	private TestEntity createTestEntity() {
		TestEntity test = new TestEntity();
		test.setCode("CODE");
		test.setDescription("Description");
		
		return test;
	}
	
	

}
