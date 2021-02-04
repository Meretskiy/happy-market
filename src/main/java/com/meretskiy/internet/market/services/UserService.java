package com.meretskiy.internet.market.services;

import com.meretskiy.internet.market.model.Role;
import com.meretskiy.internet.market.model.User;
import com.meretskiy.internet.market.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

//чтобы работал DaoAuthenticationProvider нужно implements UserDetailsService
//задача UserDetailsService - это взять нашего юзера, которого мы придумали и преобразовать его к тому
//который понимает spring security. (имя, пароль и список прав доступа) - это и есть userDetails.
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //loadUserByUsername это единственный метод, который UserDetailsService заставляет нас реализовать
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //достаем нашего юзера из БД через репозиторий
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        //создаем нового юзера спринг секьюрити класса и из нашего юзера перегоняем необходимые данные
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    //преобразовываем наши роли в те права доступа, которые понимает спринг секьюрити
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
