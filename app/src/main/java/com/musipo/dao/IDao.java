package com.musipo.dao;

import java.util.List;

public interface    IDao<Model> {

	// save object data of type <T>
	public long save(Model model);
	
	// update object data of type <T>
	public int update(Model t);
	
	// delete object data of type <T>
	public int delete(Model t);
	
	// find object data of type <T> by id
	public Model find(String id);
	
	// find object data of type <T>
	public Model find(String arg[]);
	
	// find All data of type <T>
	public List<Model> findAll();
	
}
