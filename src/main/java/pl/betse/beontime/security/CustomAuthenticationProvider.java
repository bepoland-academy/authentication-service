package pl.betse.beontime.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.betse.beontime.model.UserDTO;

import java.util.ArrayList;
import java.util.List;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Value("${usersServiceLoginURL}")
    String usersServiceLoginURL;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        RestTemplate restTemplate = new RestTemplate();
        UserDTO userDTO = restTemplate.getForObject(usersServiceLoginURL + "login?email=" + name + "&pass=" + password, UserDTO.class);
        List<SimpleGrantedAuthority> roleList = new ArrayList<>();
        assert userDTO != null;
        userDTO.getRoles().forEach(role -> roleList.add(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(name, password, roleList);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
