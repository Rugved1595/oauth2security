/**
 * 
 */
package com.afour.oauthsecurity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.afour.oauthsecurity.model.Role;

/**
 * @author rugved.m
 *
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findAllById(long i);

}
