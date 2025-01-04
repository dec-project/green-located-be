package dec.haeyum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로 허용
        registry.addMapping("/**")
                // 허용할 도메인
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","DELETE","PUT")
                .allowedHeaders("*");
    }
}
