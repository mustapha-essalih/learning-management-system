package dev.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.api.model.Categories;
import dev.api.model.Roles;
import dev.api.model.User;
import dev.api.repository.UserRepository;

@SpringBootApplication
public class Application implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;


	@Override
	public void run(String... args) throws Exception {
		
		if (!userRepository.findByEmail("admin@admin.com").isPresent()) {
			 
			List<Categories> list = new ArrayList<>();
			Categories categorie1 = Categories.builder().category("it").build();
			Categories categorie2 = Categories.builder().category("business").build();
			Categories categorie3 = Categories.builder().category("design").build();
			Categories categorie4 = Categories.builder().category("marketing").build();
			Categories categorie5 = Categories.builder().category("photography").build();
			Categories categorie6 = Categories.builder().category("lifestyle").build();
			Categories categorie7 = Categories.builder().category("health").build();
			Categories categorie8 = Categories.builder().category("fitness").build();

			list.add(categorie1);
			list.add(categorie2);
			list.add(categorie3);
			list.add(categorie4);
			list.add(categorie5);
			list.add(categorie6);
			list.add(categorie7);
			list.add(categorie8);
			
			User admin = User.builder()
			.email("admin@admin.com")
			.password(encoder.encode("1234"))
			.fullName("admin")
			.roles(Roles.ADMIN)
			.categories(list)
			.build();
	
			userRepository.save(admin);
		}

	}

}
