package com.example.myjwt;

import com.example.myjwt.models.Role;
import com.example.myjwt.models.User;
import com.example.myjwt.models.enm.ERole;
import com.example.myjwt.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication
@EntityScan(basePackageClasses = {
		MyAppApplication.class,
		Jsr310JpaConverters.class
})
@EnableScheduling

//java -jar MyApp-0.0.1-SNAPSHOT.jar com.example.myjwt.MyAppApplication
public class MyAppApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


//	@PostConstruct
//	void init() {
//		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//	}

	public static void main(String[] args) {
		SpringApplication.run(MyAppApplication.class, args);

		/*
		 * SpringApplication application = new
		 * SpringApplication(MyAppApplication.class);
		 * application.setAdditionalProfiles("ssl"); application.run(args);
		 */

	}

//	@PostConstruct
//	public  void addUsers(){
//		List<Integer> ids = new ArrayList<>(Arrays.asList(109996,164575,277785,778700));
//		List<User> users = new ArrayList();
//		for(Integer i : ids){
//			User user = new User();
//			user.setUserName(String.valueOf(i));
//			user.setEmail(String.valueOf(i)+"@gmail.com");
//			user.setIsActive(true);
//			user.setIsApproved(true);
//			user.setIsVerified(true);
//			user.setPassword(passwordEncoder.encode("Admin@123"));
//
//			Role userRole = new Role(ERole.Associate);
//
//			user.setRoles(Collections.singleton(userRole));
//			users.add(user);
//		}
//		userRepository.saveAll(users);
//	}



	/*@Bean
	public WebMvcConfigurer cors() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/*")
				.allowedHeaders("*")
				.allowedOrigins("*")
				.allowCredentials(true);
			}
		};
	}*/

}