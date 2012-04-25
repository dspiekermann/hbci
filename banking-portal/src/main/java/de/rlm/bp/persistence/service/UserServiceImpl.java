package de.rlm.bp.persistence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.rlm.bp.persistence.dao.UserDAO;
import de.rlm.bp.persistence.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Transactional
	public void addUser(User user) {
		userDAO.addUser(user);
	}

	@Transactional
	public void updateUser(User user) {
		userDAO.updateUser(user);
	}

	@Transactional
	public List<User> listUser() {
		return userDAO.listUser();
	}

	@Transactional
	public void deleteUser(int id) {
		userDAO.deleteUser(id);
	}

	@Transactional
	public User getUser(int id) {
		return userDAO.getUser(id);
	}

	@Transactional
	public User findUserByUsername(String username) {
		return userDAO.findByUsername(username);
	}

}