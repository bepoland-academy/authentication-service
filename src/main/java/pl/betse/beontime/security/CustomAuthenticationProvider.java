package pl.betse.beontime.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.betse.beontime.exception.DiscoveryServiceListIsEmptyException;
import pl.betse.beontime.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final DiscoveryClient discoveryClient;

    public CustomAuthenticationProvider(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }


    private String getUserServiceAddress() {
        if (discoveryClient.getInstances("users-service").isEmpty()) {
            log.error("Registry service list is empty!");
            throw new DiscoveryServiceListIsEmptyException();
        }
        ServiceInstance serviceInstance = discoveryClient.getInstances("users-service").get(0);
        return serviceInstance.getUri().toString();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        RestTemplate restTemplate = new RestTemplate();
        User user = restTemplate.getForObject(getUserServiceAddress() + "/login?email=" + name + "&pass=" + password, User.class);
        List<SimpleGrantedAuthority> roleList = new ArrayList<>();
        assert user != null;
        user.getRoles().forEach(role -> roleList.add(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(user, password, roleList);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
