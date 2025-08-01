package tn.esprit.spring;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

@SpringBootTest
@ActiveProfiles("test")
public class BlocServiceMockTest {

    @BeforeEach
    void beforeEach() {

    }

    @AfterEach
    void afterEach() {

    }

    @Order(1)
    @RepeatedTest(4)
    void test() {

    }

    @Order(4)
    @Test
    void test2() {

    }

    @Order(2)
    @Test
    void test3() {

    }

    @Order(3)
    @Test
    void test4() {

    }
}
