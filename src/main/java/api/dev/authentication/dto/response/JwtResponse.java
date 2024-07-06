package api.dev.authentication.dto.response;

import java.util.Date;

public class JwtResponse {
 
    private String jwt;
    private Date issuedAt;
    private Date expiration;

 

    public JwtResponse(String jwt, Date issuedAt, Date expiration) {
        this.jwt = jwt;
        this.issuedAt = issuedAt;
        this.expiration = expiration;
    }


    public String getJwt() {
        return jwt;
    }


    public void setJwt(String jwt) {
        this.jwt = jwt;
    }


    public Date getIssuedAt() {
        return issuedAt;
    }


    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }


    public Date getExpiration() {
        return expiration;
    }


    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return "JwtResponse [jwt=" + jwt + ", issuedAt=" + issuedAt + ", expiration=" + expiration + "]";
    }

}
