package com.southwind.drinkshop.config;

import com.southwind.drinkshop.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new UserFilter());
        filterRegistrationBean.addUrlPatterns("/cart/*","/orders/*","/user/userInfo","/userAddress/*");
        return filterRegistrationBean;
    }
}
