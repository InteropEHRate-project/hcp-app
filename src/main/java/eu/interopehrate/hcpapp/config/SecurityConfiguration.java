package eu.interopehrate.hcpapp.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//    @Value("${spring.security.user.name}")
//    private String username;
//    @Value("${spring.security.user.password}")
//    private String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/h2-console/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");

//        http.addFilterBefore(AutoLoginFilter.builder()
//                .authenticationManager(this.authenticationManager())
//                .username(this.username)
//                .password(this.password)
//                .build(), WebAsyncManagerIntegrationFilter.class);
    }
}
