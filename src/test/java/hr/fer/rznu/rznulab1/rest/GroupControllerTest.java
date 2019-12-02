package hr.fer.rznu.rznulab1.rest;

import hr.fer.rznu.rznulab1.RznuLab1Application;
import hr.fer.rznu.rznulab1.dto.CreateGroupDto;
import hr.fer.rznu.rznulab1.dto.CreateUserDto;
import hr.fer.rznu.rznulab1.dto.GroupDto;
import hr.fer.rznu.rznulab1.dto.UserDto;
import hr.fer.rznu.rznulab1.persistence.entity.Group;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {RznuLab1Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class GroupControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    private Group group;
    private User user;

    @Before
    public void setUp() throws Exception {
        group = new Group("TestGroup", "Test Description");
        groupRepository.save(group);

        user = new User("test2@email.com", "UserX", "XY");
        userRepository.save(user);
    }

    @After
    public void tearDown()  {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    public void should_get_all_groups() {
        ResponseEntity<List> allGroupsDtoResponseEntity = testRestTemplate.getForEntity("/api/v1/groups", List.class);

        assertThat(allGroupsDtoResponseEntity.getBody()).isNotNull();
        assertThat(allGroupsDtoResponseEntity.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void should_create_group() {
        ResponseEntity<GroupDto> groupDtoResponseEntity = testRestTemplate.postForEntity("/api/v1/groups", new CreateGroupDto("lea", "moja grupa"), GroupDto.class);

        assertThat(groupDtoResponseEntity.getBody()).isNotNull();
        assertThat(groupDtoResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(groupRepository.findById(groupDtoResponseEntity.getBody().getId())).isPresent();
    }

    @Test
    public void should_get_group() {
        ResponseEntity<GroupDto> groupDtoResponseEntity = testRestTemplate.getForEntity("/api/v1/groups/{id}", GroupDto.class, group.getId());

        assertThat(groupDtoResponseEntity.getBody()).isNotNull();
        assertThat(groupDtoResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(groupRepository.findById(groupDtoResponseEntity.getBody().getId())).isPresent();
    }

    @Test
    public void should_delete_group() {
        testRestTemplate.delete("/api/v1/groups/{id}", group.getId());

        Optional<Group> deleted = groupRepository.findById(group.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    public void should_put_group() {
        testRestTemplate.put("/api/v1/groups/{id}", new CreateGroupDto("lea", "moja grupa"), group.getId());

        Optional<Group> updated = groupRepository.findById(group.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("lea");
        assertThat(updated.get().getDescription()).isEqualTo("moja grupa");
    }

    @Test
    @Transactional
    public void should_add_user_to_group() {
        ResponseEntity response = testRestTemplate.postForEntity("/api/v1/groups/" + group.getId() + "/users/" + user.getId(), null, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(groupRepository.findById(group.getId()).get().getUsers().contains(user)).isTrue();
    }

    @Test
    @Transactional
    public void should_remove_user_from_group() {
        group.getUsers().add(user);
        groupRepository.save(group);
        user.getGroups().add(group);
        userRepository.save(user);

        testRestTemplate.delete("/api/v1/groups/" + group.getId() + "/users/" + user.getId());

        assertThat(groupRepository.findById(group.getId()).get().getUsers().contains(user)).isFalse();
    }

}