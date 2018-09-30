package com.ncu.springboot.dao;

import com.ncu.springboot.pojo.Role;
import com.ncu.springboot.pojo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * User Repository 接口.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}
