package com.peter.topset.Users.Security;

import com.peter.topset.Users.Entity.RoleEntity;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository repository;

    public MyUserDetailsService(UserRepository repository){
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found."));
        return new User(user.getEmail(),user.getPasswordHash(),mapRolesToAuthorities(user.getRoles()));
    }


    private Collection<GrantedAuthority> mapRolesToAuthorities(List<RoleEntity> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
