package de.rlm.hbci;

public class UserRequestImpl implements UserRequest {
	
	private String blz = null;
	private String userId = null;
	private String password = null;
	
	public UserRequestImpl(String blz, String userId, String password) {
		super();
		this.blz = blz;
		this.userId = userId;
		this.password = password;
	}

	public String getBlz() {
		return blz;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}
	
	

	@Override
	public String toString() {
		return "UserRequestImpl [blz=" + blz + ", userId=" + userId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blz == null) ? 0 : blz.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRequestImpl other = (UserRequestImpl) obj;
		if (blz == null) {
			if (other.blz != null)
				return false;
		} else if (!blz.equals(other.blz))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	
}
