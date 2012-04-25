package de.rlm.bp.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import de.rlm.bp.persistence.model.User;

@Repository
public class UserDAOImpl implements UserDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	public User findByUsername(String username) {
		try {
			return (User) entityManager.createNamedQuery(User.findByUsername).setParameter("username", username).getSingleResult();
		} catch (NoResultException e){
			return null;
		}
	}

	public void addUser(User user) {
		entityManager.persist(user);
	}

	public List<User> listUser() {
		return entityManager.createQuery("from User").getResultList();
	}

	public void deleteUser(int id) {
		User user = (User) entityManager.find(User.class, id);
        if (user != null) {
        	entityManager.remove(user);
        }
	}
	
	public User getUser(int id) {
		User user = (User) entityManager.find(User.class, id);
		return user;
	}

	public void updateUser(User user) {
		entityManager.merge(user);		
	}

}