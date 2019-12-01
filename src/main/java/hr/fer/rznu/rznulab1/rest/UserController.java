package hr.fer.rznu.rznulab1.rest;


import hr.fer.rznu.rznulab1.dto.CreateUserDto;
import hr.fer.rznu.rznulab1.dto.UserDto;
import hr.fer.rznu.rznulab1.persistence.entity.User;
import hr.fer.rznu.rznulab1.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Transactional
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/users")
    public ResponseEntity<UserDto> createGroup(@RequestBody @Valid CreateUserDto createUserDto) {
        Optional<User> duplicate = userRepository.findByEmail(createUserDto.getEmail());
        if (duplicate.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User(createUserDto.getEmail(), createUserDto.getName(), createUserDto.getSurname());
        userRepository.save(user);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getEmail(), user.getName(), user.getSurname()));
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> getById(
            @PathVariable("id") long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        return ResponseEntity.ok(new UserDto(user.getId(), user.getEmail(), user.getName(), user.getSurname()));
    }



    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity deleteGroup(
            @PathVariable("id") long groupId) {
        Optional<User> optionalUser = userRepository.findById(groupId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(optionalUser.get());
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> updateGroup(
            @PathVariable("id") long userId,
            @RequestBody @Valid CreateUserDto createUserDto) {
        Optional<User> optionalUser = userRepository.findByEmail(createUserDto.getName());
        if (optionalUser.isPresent() && !optionalUser.get().getId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }
        optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        user.setSurname(createUserDto.getSurname());
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());

        userRepository.save(user);

        return ResponseEntity.ok(new UserDto(user.getId(), user.getEmail(), user.getName(), user.getSurname()));
    }

}
