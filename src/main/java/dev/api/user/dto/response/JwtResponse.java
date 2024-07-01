package dev.api.user.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtResponse {
    
    private String jwt;
    private Date issuedAt;
    private Date expiration;
    @Override
    public String toString() {
        return "JwtResponse [jwt=" + jwt + ", issuedAt=" + issuedAt + ", expiration=" + expiration + "]";
    }

    
}
