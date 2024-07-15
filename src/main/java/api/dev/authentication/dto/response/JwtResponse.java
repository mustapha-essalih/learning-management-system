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




    public JwtResponse() {
		//TODO Auto-generated constructor stub
	}




	
    @Override
    public String toString() {
        return "JwtResponse [jwt=" + jwt + ", issuedAt=" + issuedAt + ", expiration=" + expiration + "]";
    }




    public String getJwt() {
        return jwt;
    }




    public Date getIssuedAt() {
        return issuedAt;
    }




    public Date getExpiration() {
        return expiration;
    }


    
}
