package ua.ukrposhta.mediabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.ukrposhta.mediabot.utils.logger.BotLogger;
import ua.ukrposhta.mediabot.utils.type.LoggerType;

@SpringBootApplication
@EnableWebMvc
public class MediabotApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(MediabotApplication.class, args);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/properties/");
		registry.addResourceHandler("/**.js").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/**.html").addResourceLocations("classpath:/templates/");
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
