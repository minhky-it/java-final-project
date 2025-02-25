package com.finalpos.POSsystem.Service;

import com.finalpos.POSsystem.Config.FirebaseService;
import com.finalpos.POSsystem.Entity.UserEntity;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Exception.ResponseHandler;
import com.finalpos.POSsystem.Model.DTO.UserDTO;
import com.finalpos.POSsystem.Model.Mapstruct.UserMapper;
import com.finalpos.POSsystem.Repository.UserRepository;
import com.finalpos.POSsystem.Service.Interface.AccountInterface;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService implements AccountInterface {
    PasswordEncoder passwordEndcoder = new BCryptPasswordEncoder();
    protected static Key JWT_Key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    @Autowired
    private FirebaseService firebase;
    @Value("${default.application.avatar}")
    private String defaultAvatar;
    @Autowired
    UserRepository db;
    UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public UserDTO createAdmin(String username, String email, String password, String name) {
        try{
            UserEntity user = new UserEntity();
            user.setRole("Administrator");
            user.setImage(defaultAvatar);
            user.setStatus("Active");
            user.setPassword(passwordEndcoder.encode(user.getPassword()));
            user.setCreated_at(java.time.LocalDateTime.now());
            user.setUsername(username);
            user.setEmail(email);
            user.setName(name);
            db.save(user);

            return mapper.toDTO(user);
        } catch (Exception e) {
            throw new FailedException("Error in " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        try{
            UserEntity user = db.findByUsername(username);
            if (user != null && passwordEndcoder.matches(password, user.getPassword())) {
                if(user.getStatus().equals("InActive")){
                    throw new FailedException("Your account is inactive!");
                }

                if(user.getStatus().equals("Lock")){
                    throw new FailedException("Your account was blocked!");
                }
                String ACCESS_TOKEN = generateToken(user);

                user.setPassword(null);
                Map<String, Object> data = new HashMap<>();
                data.put("user", mapper.toDTO(user));
                data.put("access_token", ACCESS_TOKEN);
                return data;
            } else {
                throw new FailedException("Username or password does not match!");
            }
        }catch (Exception e){
            throw new FailedException("Error in account service: " + e.getMessage());
        }
    }

    @Override
    public UserDTO changePassword(String currentPassword, String newPassword, String confirmPassword, String token) {
        try{
            if (validateToken(token)) {
                Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token).getBody();
                String username = claims.get("username", String.class);

                UserEntity user = db.findByUsername(username);
                if (user != null && passwordEndcoder.matches(currentPassword, user.getPassword())) {
                    // Update user password
                    user.setPassword(passwordEndcoder.encode(newPassword));
                    UserEntity result = db.save(user);

                    return mapper.toDTO(result);
                } else {
                    throw new FailedException("Incorrect current password");
                }
            } else {
                throw new FailedException("Invalid token");
            }
        }catch (Exception e){
            throw new FailedException("Error in account service: " + e.getMessage());
        }
    }

    @Override
    public UserDTO viewProfile(String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token).getBody();
            String username = claims.get("username").toString();
            UserEntity user = db.findByUsername(username);
            if(user == null)
                throw new FailedException("This user does not exist!");

            return mapper.toDTO(user);
        } catch (Exception e) {
            throw new FailedException("Error in account service: " + e.getMessage());
        }
    }

    @Override
    public UserDTO updateProfile(String name, Optional<MultipartFile> file, String token) {
        try{
            // Parse the authentication token to extract the username
            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token).getBody();
            String username = claims.get("username").toString();
            // Retrieve the user's profile information from the database
            UserEntity user = db.findByUsername(username);
            // Update the image URL if necessary
            if (file.isPresent()) {
                String imageUrl = firebase.uploadImage(file.get());
                user.setImage(imageUrl);
            }
            // Update the username if necessary
            if (!user.getName().equals(name))
                user.setName(name);
            // Save the updated profile information
            UserEntity result = db.save(user);
            // Return a success message with the updated user information
            return mapper.toDTO(result);
        } catch (Exception e) {
            throw new FailedException("Error in account service: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> loginDirectly(String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token).getBody();
            String username = claims.get("username").toString();
            UserEntity user = db.findByUsername(username);

            if(user == null)
                throw new FailedException("Failed to login with this token");

            String generatedToken = generateToken(user);

            if(user.getStatus().equals("InActive"))
                throw new FailedException("Your account is inactive");

            if(user.getStatus().equals("Lock"))
                throw new FailedException("Your account is locked");

            Map<String, Object> data = new HashMap<>();
            data.put("user", mapper.toDTO(user));
            data.put("token", generatedToken);
            return data;
        } catch (Exception e) {
            throw new FailedException("Error in account service: " + e.getMessage());
        }
    }

    @Override
    public UserDTO renewPassword(String password, String confirm, String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token).getBody();
            String username = claims.get("username").toString();
            UserEntity user = db.findByUsername(username);

            if(user == null)
                throw new FailedException("This user does not exist!");

            String hashedPassword = passwordEndcoder.encode(password);
            user.setPassword(hashedPassword);
            user.setStatus("Active");
            UserEntity result = db.save(user);
            return mapper.toDTO(result);
        } catch (Exception e) {
            throw new FailedException("Error in account service: " + e.getMessage());
        }
    }

    private static String generateToken(UserEntity user) {
        Date expirationTime = new Date(System.currentTimeMillis() + 3600000);
        return Jwts.builder()
                .claim("_id", user.getId())
                .claim("username", user.getUsername())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("image", user.getImage())
                .claim("role", user.getRole())
                .claim("status", user.getStatus())
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, JWT_Key)
                .compact();
    }

    private static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
