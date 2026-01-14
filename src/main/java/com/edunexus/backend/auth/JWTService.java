package com.edunexus.backend.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTService {
	private static final String SECRET = "SuperSecretEduNexusKeyThatIsAtLeast32Chars";
	
	private static final long EXPIRATION_MILLIS = 1000 * 60 * 60 * 2; 
	
	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)); //converts the secret key to UTF-8 format
	}
	
	public String extractEduId(String token) {
	    return validateAndExtract(token).getSubject();  // subject = userId (eduId)
	}

	public String extractRole(String token) {
	    Object role = validateAndExtract(token).get("role");
	    return role == null ? null : role.toString();
	}

	public String generateToken(String userId, String role) { //will generate a json token as user logins with the credentials
		Date now = new Date(); //get the date object
		Date expiry = new Date(now.getTime() + EXPIRATION_MILLIS);
		
		return Jwts.builder().setSubject(userId).claim("role", role).setIssuedAt(now).setExpiration(expiry).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}
	
		public Claims validateAndExtract(String token) { //use the token generated and parseIt and checks if it 
			return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
		}
	
}
