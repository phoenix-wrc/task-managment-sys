package site.ph0en1x.task_management_sys.web.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import site.ph0en1x.task_management_sys.model.user.Roles;
import site.ph0en1x.task_management_sys.model.user.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtEntityFactory {
    public static JwtEntity create(User user) {
        return new JwtEntity(
                user.getId(),
                user.getEmail(),
//                user.getFirstName() + " " + user.getLastName(),
                user.getPassword(),
                mapToGrantedAuthorities(new HashSet<>(user.getRoles()))
        );
    }

    private static List<SimpleGrantedAuthority> mapToGrantedAuthorities(Set<Roles> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}