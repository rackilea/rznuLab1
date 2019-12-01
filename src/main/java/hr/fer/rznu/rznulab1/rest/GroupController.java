package hr.fer.rznu.rznulab1.rest;

import hr.fer.rznu.rznulab1.dto.CreateGroupDto;
import hr.fer.rznu.rznulab1.dto.GroupDto;
import hr.fer.rznu.rznulab1.dto.UserDto;
import hr.fer.rznu.rznulab1.persistence.entity.Group;
import hr.fer.rznu.rznulab1.persistence.entity.User;
import hr.fer.rznu.rznulab1.persistence.repository.GroupRepository;
import hr.fer.rznu.rznulab1.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Transactional
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/groups")
    public ResponseEntity<GroupDto> createGroup(@RequestBody @Valid CreateGroupDto createGroupDto) {
        Optional<Group> duplicate = groupRepository.findByName(createGroupDto.getName());
        if (duplicate.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        Group group = new Group(createGroupDto.getName(), createGroupDto.getDescription());
        groupRepository.save(group);
        return ResponseEntity.ok(new GroupDto(group.getId(), group.getName(), group.getDescription()));
    }

    @GetMapping(path = "/groups/{id}")
    public ResponseEntity<GroupDto> getById(
            @PathVariable("id") long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (!optionalGroup.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Group group = optionalGroup.get();
        return ResponseEntity.ok(new GroupDto(group.getId(), group.getName(), group.getDescription()));
    }

    @DeleteMapping(path = "/groups/{id}")
    public ResponseEntity deleteGroup(
            @PathVariable("id") long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (!optionalGroup.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        groupRepository.delete(optionalGroup.get());
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/groups/{id}")
    public ResponseEntity<GroupDto> updateGroup(
            @PathVariable("id") long groupId,
            @RequestBody @Valid CreateGroupDto createGroupDto) {
        Optional<Group> groupOptional = groupRepository.findByName(createGroupDto.getName());
        if (groupOptional.isPresent() && !groupOptional.get().getId().equals(groupId)) {
            return ResponseEntity.badRequest().build();
        }
        groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Group group = groupOptional.get();
        group.setName(createGroupDto.getName());
        group.setDescription(createGroupDto.getDescription());

        groupRepository.save(group);

        return ResponseEntity.ok(new GroupDto(group.getId(), group.getName(), group.getDescription()));
    }

    @GetMapping(path = "/groups/{id}/users")
    public ResponseEntity<List> getGroupUsers(
            @PathVariable("id") long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (!optionalGroup.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Group group = optionalGroup.get();
        //return all users from groups
        List<UserDto> userDtoList = group.getUsers().stream().map(user -> new UserDto(user.getId(), user.getEmail(), user.getName(), user.getSurname())).collect(Collectors.toList());
        return ResponseEntity.ok(userDtoList);
    }

    @PostMapping(path = "/groups/{groupId}/users/{userId}")
    public ResponseEntity addUserToGroup(@PathVariable("groupId") long groupId,
                                         @PathVariable("userId") long userId
    ) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Group group = groupOptional.get();
        User user = optionalUser.get();
        group.getUsers().add(user);
        groupRepository.save(group);
        user.getGroups().add(group);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/groups/{groupId}/users/{userId}")
    public ResponseEntity removeUserFromGroup(@PathVariable("groupId") long groupId,
                                              @PathVariable("userId") long userId
    ) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Group group = groupOptional.get();
        group.getUsers().remove(optionalUser.get());
        groupRepository.save(group);
        return ResponseEntity.ok().build();
    }


}
