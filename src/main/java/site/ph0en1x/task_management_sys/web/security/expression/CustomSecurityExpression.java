package site.ph0en1x.task_management_sys.web.security.expression;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.ph0en1x.task_management_sys.model.user.Roles;
import site.ph0en1x.task_management_sys.service.UserService;
import site.ph0en1x.task_management_sys.web.security.JwtEntity;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        return user.getId();
    }

    public boolean canAccessUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getCurrentUserId().equals(id) || hasAnyRole(authentication, Roles.ROLE_ADMIN);
    }

    public boolean canAccessSetStatus(Long taskId) {
        return userService.isTaskExecutor(getCurrentUserId(), taskId);
    }

    private boolean hasAnyRole(Authentication authentication, Roles ... roles) {
        for(Roles role: roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }
}
