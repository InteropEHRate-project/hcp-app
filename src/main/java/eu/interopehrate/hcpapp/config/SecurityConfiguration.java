package eu.interopehrate.hcpapp.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/administration/vital-signs-nomenclature/**", "/h2-console/**").hasRole("ADMIN")
                .anyRequest().hasAnyRole("DOCTOR", "ADMIN")
                .and()
                .httpBasic()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");

        http.formLogin()
                .and()
                .logout();
    }
}
