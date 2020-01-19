package project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import project.photo.boundary.StorageProperties;
import project.photo.boundary.StorageService;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class HackhatonPhotoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HackhatonPhotoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
