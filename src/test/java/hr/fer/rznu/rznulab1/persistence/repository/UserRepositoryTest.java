package hr.fer.rznu.rznulab1.persistence.repository;

import hr.fer.rznu.rznulab1.RznuLab1Application;
import hr.fer.rznu.rznulab1.persistence.entity.Group;
import hr.fer.rznu.rznulab1.persistence.entity.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {RznuLab1Application.class})
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    public void should_create_user() {
        User user = new User();
        user.setEmail("email");
        user.setName("name");
        user.setSurname("surname");

        userRepository.save(user);


        Group group = new Group();
        group.setDescription("description");
        group.setName("name");

        groupRepository.save(group);

        user.getGroups().add(group);
        userRepository.save(user);

        assertThat(user.getId()).isNotNull();
    }

}