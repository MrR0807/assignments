package lt.laurynas.uzduotis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class UzduotisApplication {

    public static void main(String[] args) {
        SpringApplication.run(UzduotisApplication.class, args);
    }

}
