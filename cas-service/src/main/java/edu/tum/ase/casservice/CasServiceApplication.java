package edu.tum.ase.casservice;

import com.mongodb.client.MongoClient;
import edu.tum.ase.casservice.model.UserRole;
import edu.tum.ase.casservice.repository.UserRepository;
import edu.tum.ase.casservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {UserRepository.class})
public class CasServiceApplication  implements CommandLineRunner {


	private static final Logger log = LoggerFactory.getLogger(CasServiceApplication.class);
	@Autowired
	MongoClient mongoClient;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(CasServiceApplication.class, args);
	}
	// 预请求
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/").allowedOrigins("http://localhost:3000").allowCredentials(true).allowedHeaders("*").allowedMethods("*");
//			}
//		};
//	}

	@Override
	public void run(String... args) throws Exception {
		log.info("MongoClient = "+mongoClient.getClusterDescription());
		// should create user for testing！！！  3 role
		if (userRepository.findAll().size() == 0) {
			String pwd = bCryptPasswordEncoder.encode("123");
//			System.out.println (bCryptPasswordEncoder.encode ("referrerPolicy"));
			userService.createUserForTest("dispatcher1@gmail.com", pwd, UserRole.DISPATCHER);
			userService.createUserForTest("Jone", pwd, UserRole.DELIVERER);
			userService.createUserForTest("Jane", pwd, UserRole.CUSTOMER);
		}
	}


}
