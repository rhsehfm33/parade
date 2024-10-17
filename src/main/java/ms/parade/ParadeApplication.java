package ms.parade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ParadeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParadeApplication.class, args);
    }

}
