package com.spring.recipe.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Buukie on 9/15/2018.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByUserId(String id);
    Group findByName(String name);
}
