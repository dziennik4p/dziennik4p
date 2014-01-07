package net.forprogrammers.dziennik.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.forprogrammers.dziennik.util.JPAUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4957674554799005335L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@NotNull
	private String firstname;

	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new Users().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countUsers() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Users o",
				Long.class).getSingleResult();
	}

	public static List<Users> findAllUsers() {
		return entityManager()
				.createQuery("SELECT o FROM Users o", Users.class)
				.getResultList();
	}
	
	public static Users findUsers(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Users.class, id);
	}

	public static Users findByUsername(String username) {
		TypedQuery<Users> q = entityManager()
				.createQuery("from Users u where u.username = :username", Users.class)
				.setParameter("username", username);
		Users user = JPAUtils.getSingleResultOrNull(q);
		return user;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void add() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Users user = this;
		this.entityManager.persist(user);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Users attached = Users.findUsers(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public Users merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Users merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	public String toString() {
		return firstname;
	}
}
