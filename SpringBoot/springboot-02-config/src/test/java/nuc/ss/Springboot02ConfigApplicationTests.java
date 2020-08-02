package nuc.ss;

import nuc.ss.pojo.Dog;
import nuc.ss.pojo.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Springboot02ConfigApplicationTests {

    @Autowired
    private Dog dog;

    @Autowired
    private Person person;
    @Test
    void contextLoads() {
        //System.out.println(dog);
        System.out.println(person);
    }

}
