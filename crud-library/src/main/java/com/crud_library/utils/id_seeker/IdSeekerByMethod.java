package com.crud_library.utils.id_seeker;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdSeekerByMethod extends IdSeeker {

	@Override
	public Object getId(Object entity) {
		Method methodId = this.getIdMehtod(entity);
		
		if(methodId == null) return this.checkNext(entity);
		
		try {
			log.info("Method id found: {}.{}", entity.getClass().getName(), methodId.getName());
			return this.getMethodValue(methodId, entity);
		
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error("Unable to get id from method {}.{}: {}", entity.getClass().getName(), methodId.getName(), e.getMessage());
		}
		return null;
	}

	private Object getMethodValue(Method methodId, Object entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return methodId.invoke(entity, new Object[0]);
	}

	private Method getIdMehtod(Object entity) {
		for(Method currentMethod: entity.getClass().getDeclaredMethods()) {
			for(Annotation currentAnnotation : currentMethod.getDeclaredAnnotations()) {
				log.info("annotation for method {}: {}", currentMethod.getName(), currentAnnotation);
				if(currentAnnotation.toString().contains(".Id(")) {
					return currentMethod;
				}
				
			}
		}
		return null;
	}

}
