package com.crud_library.utils.id_seeker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdSeekerByField extends IdSeeker {

	@Override
	public Object getId(Object entity) {
		Field fieldId = this.getIdField(entity);
		
		if(fieldId == null) return this.checkNext(entity);
		
		try {
			log.info("Field id found: {}.{}", entity.getClass().getName(), fieldId.getName());
			return getFieldValue(fieldId, entity);
		
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("Unable to get id from field {}.{}: {}", entity.getClass().getName(), fieldId.getName(), e.getMessage());
		}
		
		return null;
	}
	
	private Object getFieldValue(Field fieldId, Object entity) throws IllegalArgumentException, IllegalAccessException {
		fieldId.setAccessible(true);

		return fieldId.get(entity);
	}

	private Field getIdField(Object entity) {
		for(Field currentField: entity.getClass().getDeclaredFields()) {
			for(Annotation currentAnnotation : currentField.getDeclaredAnnotations()) {
				if(currentAnnotation.toString().contains("Id")) {
					return currentField;
				}
				
			}
		}
		return null;
	}

}
