
package com.mycompany.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mycompany.formatters.HotelFormatter;
import com.mycompany.formatters.LocationFormatter;
import com.mycompany.formatters.ProviderFormatter;
import com.mycompany.formatters.DateFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@PropertySource("classpath:configs.properties")
@ComponentScan(
    basePackages = {
        "com.mycompany.controllers",
        "com.mycompany.repositories",
        "com.mycompany.services",
        "com.mycompany.formatters"
    }
)
@EnableWebMvc
@EnableTransactionManagement
public class WebAppContextConfigs implements WebMvcConfigurer {
    @Value("${cloudinary.cloud_name:${CLOUDINARY_CLOUD_NAME:}}")
    private String cloudinaryCloudName;
    @Value("${cloudinary.api_key:${CLOUDINARY_API_KEY:}}")
    private String cloudinaryApiKey;
    @Value("${cloudinary.api_secret:${CLOUDINARY_API_SECRET:}}")
    private String cloudinaryApiSecret;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocationFormatter());
        registry.addFormatter(new ProviderFormatter());
        registry.addFormatter(new HotelFormatter());
        registry.addFormatter(new DateFormatter());
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", this.cloudinaryCloudName,
                "api_key", this.cloudinaryApiKey,
                "api_secret", this.cloudinaryApiSecret,
                "secure", true));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");    }
}
