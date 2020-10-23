package eu.interopehrate.hcpapp.security;

import eu.interopehrate.hcpapp.jpa.entities.UserEntity;
import eu.interopehrate.hcpapp.jpa.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<UserEntity> user = this.userRepository.findByUsername(name);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + name));
        return new MyUserDetails(user.get());
    }
}
