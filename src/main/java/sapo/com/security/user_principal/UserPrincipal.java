package sapo.com.security.user_principal;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sapo.com.model.entity.User;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Builder
public class UserPrincipal implements UserDetails {
    private Long id ;
    private String email ;
    private String name ;
    private String password ;
    private Boolean status ;
    private User user ;
    private Collection<?extends GrantedAuthority> authorities ;


    public static UserDetails build(User user){
        return UserPrincipal.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .status(user.getStatus())
                .authorities(user.getRoles().stream().map(item-> new SimpleGrantedAuthority(item.getName().toString())).toList()).build();
    }
    @Override
    public  Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (this.status == null){
            this.status = false ;
            return false ;
        }
        return this.status;
    }
}
