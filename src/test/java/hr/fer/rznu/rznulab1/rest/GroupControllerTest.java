package hr.fer.rznu.rznulab1.rest;

import hr.fer.rznu.rznulab1.RznuLab1Application;
import hr.fer.rznu.rznulab1.dto.CreateGroupDto;
import hr.fer.rznu.rznulab1.dto.GroupDto;
import hr.fer.rznu.rznulab1.persistence.entity.Group;
import hr.fer.rznu.rznulab1.persistence.repository.GroupRepository;
import hr.fer.rznu.rznulab1.persistence.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Before
    public void setUp() throws Exception {
        group = new Group("group1", "description");
        groupRepository.save(group);
    }

    @After
    public void tearDown()  {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    public void shuold_create_group() {
        ResponseEntity<GroupDto> groupDtoResponseEntity = testRestTemplate.postForEntity("/groups", new CreateGroupDto("lea", "moja grupa"), GroupDto.class);

        assertThat(groupDtoResponseEntity.getBody()).isNotNull();
        assertThat(groupDtoResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(groupRepository.findById(groupDtoResponseEntity.getBody().getId())).isPresent();
    }

    @Test
    public void should_get_group() {
        ResponseEntity<GroupDto> groupDtoResponseEntity = testRestTemplate.getForEntity("/groups/{id}", GroupDto.class, group.getId());

        assertThat(groupDtoResponseEntity.getBody()).isNotNull();
        assertThat(groupDtoResponseEntity.getStatusCode().value()).isEqualTo(200);
        assertThat(groupRepository.findById(groupDtoResponseEntity.getBody().getId())).isPresent();
    }

    @Test
    public void should_delete_group() {
        testRestTemplate.delete("/groups/{id}", group.getId());

        Optional<Group> deleted = groupRepository.findById(group.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    public void should_put_group() {
        testRestTemplate.put("/groups/{id}", new CreateGroupDto("lea", "moja grupa"), group.getId());

        Optional<Group> updated = groupRepository.findById(group.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("lea");
        assertThat(updated.get().getDescription()).isEqualTo("moja grupa");


    }

}