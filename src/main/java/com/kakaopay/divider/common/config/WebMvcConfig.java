package com.kakaopay.divider.common.config;

import com.kakaopay.divider.money.interceptor.MoneyRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MoneyRequestInterceptor())
					.addPathPatterns("/money/**");
	}
}
