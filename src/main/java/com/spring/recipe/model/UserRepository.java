package com.spring.recipe.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Buukie on 9/16/2018.
 */
public interface UserRepository extends JpaRepository<User, String> {

}
