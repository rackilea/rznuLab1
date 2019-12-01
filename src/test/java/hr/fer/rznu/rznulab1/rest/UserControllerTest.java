package hr.fer.rznu.rznulab1.rest;

import hr.fer.rznu.rznulab1.RznuLab1Application;
import hr.fer.rznu.rznulab1.persistence.repository.GroupRepository;
import hr.fer.rznu.rznulab1.persistence.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {RznuLab1Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }

}