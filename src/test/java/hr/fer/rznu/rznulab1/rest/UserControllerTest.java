package hr.fer.rznu.rznulab1.rest;

import hr.fer.rznu.rznulab1.RznuLab1Application;
import hr.fer.rznu.rznulab1.dto.CreateUserDto;
import hr.fer.rznu.rznulab1.dto.UserDto;
import hr.fer.rznu.rznulab1.persistence.entity.User;
import hr.fer.rznu.rznulab1.persistence.repository.GroupRepository;
import hr.fer.rznu.rznulab1.persistence.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {RznuLab1Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("test@test.com", "Lea", "Racki");
        userRepository.save(user);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    public void should_get_all_users() {
        ResponseEntity<List> allUsersDtoResponseEntity = testRestTemplate.getForEntity("/api/v1/users", List.class);

        assertThat(allUsersDtoResponseEntity.getBody()).isNotNull();
        assertThat(allUsersDtoResponseEntity.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void should_not_have_empty_users_list() {
        ResponseEntity<List> allUsersDtoResponseEntity = testRestTemplate.getForEntity("/api/v1/users", List.class);

        assertThat(allUsersDtoResponseEntity.getBody()).isNotNull();
        assertThat(allUsersDtoResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(allUsersDtoResponseEntity.getBody().size()).isEqualTo(1);
    }

    @Test
    public void should_create_user() {
        ResponseEntity<UserDto> userDtoResponseEntity = testRestTemplate.postForEntity("/api/v1/users", new CreateUserDto("Name", "Surname", "test@email.com"), UserDto.class);

        assertThat(userDtoResponseEntity.getBody()).isNotNull();
        assertThat(userDtoResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(userRepository.findById(userDtoResponseEntity.getBody().getId())).isPresent();
    }

    @Test
    public void should_get_user() {
        ResponseEntity<UserDto> userDtoResponseEntity = testRestTemplate.getForEntity("/api/v1/users/{id}", UserDto.class, user.getId());

        assertThat(userDtoResponseEntity.getBody()).isNotNull();
        assertThat(userDtoResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(userRepository.findById(userDtoResponseEntity.getBody().getId())).isPresent();
    }

    @Test
    public void should_delete_user() {
        testRestTemplate.delete("/api/v1/users/{id}", user.getId());

        Optional<User> deleted = userRepository.findById(user.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    public void should_put_user() {
        testRestTemplate.put("/api/v1/users/{id}", new CreateUserDto("UserX", "XY", "test2@email.com"), user.getId());

        Optional<User> updated = userRepository.findById(user.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("UserX");
        assertThat(updated.get().getSurname()).isEqualTo("XY");
        assertThat(updated.get().getEmail()).isEqualTo("test2@email.com");


    }

}
