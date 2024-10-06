package um.fds.agl.ter24.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import um.fds.agl.ter24.entities.UserTER;
import um.fds.agl.ter24.repositories.UserTERRepository;

@Component
public class SpringDataJpaUserDetailsService implements UserDetailsService {

    private final UserTERRepository repository;

    @Autowired
    public SpringDataJpaUserDetailsService(UserTERRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserTER user = this.repository.findByLastName(name);
        if (user == null) {
            throw new UsernameNotFoundException(name);
        }
        return new User(user.getLastName(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRoles()));
    }

}
