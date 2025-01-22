package dec.haeyum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HaeyumApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaeyumApplication.class, args);
	}

}
