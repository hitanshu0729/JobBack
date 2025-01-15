package net.engineeringdigest.journalApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

// first priority to cli then application.properties then application.yml
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
// add the JounalConfig doc in db which have the weather api key
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class JobApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
    @Bean
    public PlatformTransactionManager falana(MongoDatabaseFactory dbFactory){
        return new MongoTransactionManager(dbFactory);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}