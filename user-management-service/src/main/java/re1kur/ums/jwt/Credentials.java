package re1kur.ums.jwt;


import lombok.Value;
import re1kur.ums.entity.Role;
import re1kur.ums.entity.User;

import java.util.HashMap;
import java.util.Map;

@Value
public class Credentials {
    Map<String, String> claims;

    public Credentials(User user) {
        claims = new HashMap<>();
        claims.put("sub", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("enabled", user.getEnabled().toString());
        claims.put("scope", user.getRoles().stream()
                .map(Role::getName)
                .toList()
                .toString().replace("[", "").replace("]", ""));
    }
}
