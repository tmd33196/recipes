package com.spring.recipe.web;

import com.spring.recipe.model.Group;
import com.spring.recipe.model.GroupRepository;
import com.spring.recipe.model.User;
import com.spring.recipe.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Buukie on 9/15/2018.
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class GroupController {
    private GroupRepository groupRepository;
    private UserRepository userRepository;

    public GroupController(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/groups")
    Collection<Group> groups(Principal principal) {
        log.info("Request to get all groups for user: {}", principal);
        return groupRepository.findAllByUserId(principal.getName());
    }

    @GetMapping("/group/{id}")
    ResponseEntity<?> getGroup(@PathVariable Long id) {
        log.info("Request to get group: {}", id);
        Optional<Group> group = groupRepository.findById(id);
        return group.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/group")
    ResponseEntity<Group> createGroup(@Valid @RequestBody Group group,
                                      @AuthenticationPrincipal OAuth2User principal) throws URISyntaxException {
        log.info("Request to create group: {}\n for principal: {}", group, principal);

        Map<String, Object> details = principal.getAttributes();
        String userId = details.get("sub").toString();

        Optional<User> user = userRepository.findById(userId);
        group.setUser(user
                .orElse(new User(userId, details.get("name").toString(), details.get("email").toString())));

        Group result = groupRepository.save(group);
        return ResponseEntity.created(new URI("/api/group/" + result.getId()))
                .body(result);
    }

    @PutMapping("/group/{id}")
    ResponseEntity<Group> updateGroup(@PathVariable Long id, @Valid @RequestBody Group group) {
        log.info("Request to update group: {}", group);
        group.setId(id);
        log.info("Request to update group: {}", group);
        Group result = groupRepository.save(group);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/group/{id}")
    ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        log.info("Request to delete the group: {}", id);
        groupRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
