package hr.fer.rznu.rznulab1.persistence.repository;

import hr.fer.rznu.rznulab1.RznuLab1Application;
import hr.fer.rznu.rznulab1.persistence.entity.Group;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {RznuLab1Application.class})
@RunWith(SpringRunner.class)
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    public void should_save_group() {
        Group group = new Group();
        group.setDescription("description");
        group.setName("name");

        groupRepository.save(group);
    }
}