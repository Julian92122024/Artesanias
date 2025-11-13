package com.artesania.santander.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//Esta clase se encarga de configurar todo lo relacionado con el login, la pagina de login, las paginas que necesitan autenticación y los roles
	
	@Autowired
	private userDetailsServiceImpl userDetailsService; 
	
	//Metodo para encriptar contraseñas
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//Configuramos los parametros de autenticación
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	//Definimos las URL que necesitan y no necesitan autenticación, se asigna la URL del formulario del login junto con la URL del logout
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/","/index","/nosotros","/artesanias","/quienesSomos","/quienesSomos/*","/uploads/*","/artesanias/*","/sendMail","/productos","/contactenos","/public/**","/js/**","/css/**","/img/**").permitAll().anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/login").defaultSuccessUrl("/admin",true).failureUrl("/login?error=true")
			.loginProcessingUrl("/admin/login-post").permitAll()
		.and()
			.logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll();
	}
	
	

}
