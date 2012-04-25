package de.rlm.bp.persistence.dao;

import java.util.List;

import de.rlm.bp.persistence.model.User;

public interface UserDAO {

	public void addUser(User user);
	public void updateUser(User user);
	public List<User> listUser();
	public void deleteUser(int id);
	public User getUser(int id);
	
	public User findByUsername(String username);
	
	
}