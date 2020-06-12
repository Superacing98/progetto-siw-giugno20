package it.uniroma3.siw.giugno20.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static it.uniroma3.siw.giugno20.model.Credentials.ADMIN_ROLE;

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception{
		httpSecurity
					.authorizeRequests()
					.antMatchers(HttpMethod.GET, "/", "/index", "/login", "/user/register").permitAll()
					.antMatchers(HttpMethod.POST, "/login", "/user/register").permitAll()
					.antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
					.antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
					.anyRequest().authenticated()
					.and().formLogin()
					.defaultSuccessUrl("/home")
					.and().logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/index")
					.invalidateHttpSession(true)
					.clearAuthentication(true).permitAll();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
		managerBuilder
						.jdbcAuthentication()
						.dataSource(this.dataSource)
						.authoritiesByUsernameQuery("SELECT user_name, role FROM credentials WHERE user_name=?")
						.usersByUsernameQuery("SELECT user_name, password, 1 as enabled FROM credentials WHERE user_name=?");
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
