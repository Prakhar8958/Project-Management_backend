package com.example.fullstack.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

public class JwtProvider {

      static SecretKey Key= Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

      public static String generateToken(Authentication auth){

          return Jwts.builder().setIssuedAt(new Date())
                  .setExpiration(new Date(new Date().getTime()+86400000))
                  .claim("email",auth.getName())
                  .signWith(Key)
                  .compact();
      }

      public static String getEmailFromToken(String jwt){
          Claims claims= Jwts.parserBuilder().setSigningKey(Key).build().parseClaimsJws(jwt).getBody();
          return String.valueOf(claims.get("email"));
      }

}
