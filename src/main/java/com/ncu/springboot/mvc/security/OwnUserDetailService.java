package com.ncu.springboot.mvc.security;

import com.ncu.springboot.dao.RoleRepository;
import com.ncu.springboot.dao.UserRepository;
import com.ncu.springboot.pojo.Role;
import com.ncu.springboot.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OwnUserDetailService implements UserDetailsService {
      @Autowired
      PasswordEncoder passwordEncoder;
      @Autowired
      private UserRepository userRepository;
      @Autowired
      RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        long Id = Long.parseLong(name);
        Optional<User> user = userRepository.findById(Id);
        if (user.get() == null){
            throw new AuthenticationCredentialsNotFoundException("authError");
        }
        log.info("{}",user);
        List<Role> roles = user.get().getRoles().stream().collect(Collectors.toList());
        log.info("{}",roles);
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role1 -> authorities.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_"+role1.getName().toUpperCase())));
        log.info("{}",authorities);
        return new org.springframework.security.core.userdetails.User(user.get().getName(),user.get().getPwd(),authorities);

    }
}
