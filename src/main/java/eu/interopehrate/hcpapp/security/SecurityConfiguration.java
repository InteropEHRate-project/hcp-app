package eu.interopehrate.hcpapp.security;

import eu.interopehrate.hcpapp.jpa.entities.UserEntity;
import eu.interopehrate.hcpapp.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(@Qualifier("myUserDetailsService") UserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String hashedPassAdmin = passwordEncoder.encode("admin");
        String hashedPassDoctor = passwordEncoder.encode("doctor");

        UserEntity doctor = new UserEntity();
        doctor.setUsername("doctor");
        doctor.setPassword(hashedPassDoctor);
        doctor.setActive(true);
        doctor.setAuthorities("DOCTOR");

        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(hashedPassAdmin);
        admin.setActive(true);
        admin.setAuthorities("ADMIN");

        userRepository.save(doctor);
        userRepository.save(admin);
        System.out.println();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/administration/vital-signs-nomenclature/**", "/h2-console/**").hasAuthority("ADMIN")
                .anyRequest().hasAnyAuthority("DOCTOR", "ADMIN")
                .and()
                .formLogin()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
