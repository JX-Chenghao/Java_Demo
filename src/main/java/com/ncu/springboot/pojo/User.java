package com.ncu.springboot.pojo;

import lombok.Data;

import java.util.Set;

/**
 * User 实体.
 */

@Data
//@Entity
public class User {
	//@Id // 主键
	//@GeneratedValue(strategy=GenerationType.IDENTITY) // 自增策略
	private Long id; // 实体一个唯一标识
	private String name;
	private String email;
	private String pwd;
	/*@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	private Set<Role> roles;*/
	private Set<Role> roles;

	protected User() { // 无参构造函数;设为 protected 防止直接使用
	}
	
	public User(Long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public User(String name, String email, String pwd) {
		this.name = name;
		this.email = email;
		this.pwd = pwd;
	}

	public String getCredentialSalt(){
		return getName()+"123";
	}
}
