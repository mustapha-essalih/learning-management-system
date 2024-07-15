package api.dev;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;

import api.dev.admin.model.Admin;
import api.dev.authentication.repository.UserRepository;
import api.dev.courses.model.Categories;


@EnableAspectJAutoProxy
@SpringBootApplication
public class ApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;

	
	@Override
	public void run(String... args) throws Exception {
		
		if (!userRepository.findByEmail("admin@admin.com").isPresent()) {
			 
			List<Categories> list = new ArrayList<>();
			Categories categorie1 = new Categories("it");
			Categories categorie2 = new Categories("business");
			Categories categorie3 = new Categories("design");
			Categories categorie4 = new Categories("marketing");
			Categories categorie5 = new Categories("photography");
			Categories categorie6 = new Categories("lifestyle");
			Categories categorie7 = new Categories("health");
			Categories categorie8 = new Categories("fitness");

			list.add(categorie1);
			list.add(categorie2);
			list.add(categorie3);
			list.add(categorie4);
			list.add(categorie5);
			list.add(categorie6);
			list.add(categorie7);
			list.add(categorie8);
			
			Admin admin = new Admin("admin@admin.com" , encoder.encode("1234") , "ADMIN" , "supper user", list);
			 
	
			userRepository.save(admin);
		}

	}

}
