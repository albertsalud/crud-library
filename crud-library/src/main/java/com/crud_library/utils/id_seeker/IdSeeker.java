package com.crud_library.utils.id_seeker;

public abstract class IdSeeker {
	
	private IdSeeker next;
	
	public static IdSeeker set(IdSeeker first, IdSeeker... next) {
		IdSeeker head = first;
		for(IdSeeker nextInChain: next) {
			head.next = nextInChain;
			head = nextInChain;
		}
		
		return first;
	}
	
	public abstract Object getId(Object entity);
	
	protected Object checkNext(Object entity) {
		if(next != null) {
			return next.getId(entity);
		}
		
		return null;
	}

}
