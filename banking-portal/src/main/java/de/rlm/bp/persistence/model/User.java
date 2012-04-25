package de.rlm.bp.persistence.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

@Entity
@Table(name="USERS",
	   uniqueConstraints = {
			   @UniqueConstraint(columnNames={"username"}),
			   @UniqueConstraint(columnNames={"email"})
	   }
)
@NamedQueries({
	@NamedQuery(name=User.findAll,query="SELECT user from User user"),
	@NamedQuery(name=User.findByUsername,query="SELECT user from User user where username = :username"),
	@NamedQuery(name=User.findByEmail,query="SELECT user from User user where email = :email"),
	@NamedQuery(name=User.findByAutocomplete,query="SELECT user from User user where user.username like :query or user.name like :query or user.surname like :query")
})
public class User extends EntityAbstract {
	
	private static final long serialVersionUID = 1L;
	
	public final static String findAll = "de.rlm.bp.persistence.model.User.findAll";
	public final static String findByUsername = "de.rlm.bp.persistence.model.User.findByUsername";
	public final static String findByEmail = "de.rlm.bp.persistence.model.User.findByEmail";
	public final static String findByAutocomplete = "de.rlm.bp.persistence.model.User.findByAutocomplete";
	
	@Column(nullable=false)
	@Index(name="idx_user_username")
	private String username = null;
	@Column(nullable=false)
	private String password = null;
	private String name = null;
	private String surname = null;
	@Column(nullable=false)
	private String email = null;
	@Column(name="LAST_LOGIN")
	private Date lastLogin = null;
	@Basic(fetch=FetchType.EAGER)
	@Lob
	private byte[] image = null;
	@Column(nullable=false)
	private Boolean enabled = false;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullname() {
		StringBuffer b = new StringBuffer();
		if (surname!=null){
			b.append(surname);
		}
		if (b.length()>0){
			b.append(" ");
		}
		if (name!=null){
			b.append(name);
		}
		return b.toString();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
