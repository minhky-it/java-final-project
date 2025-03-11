package com.finalpos.POSsystem.Service;

import com.finalpos.POSsystem.Entity.UserEntity;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Model.DTO.UserDTO;
import com.finalpos.POSsystem.Model.Mapstruct.UserMapper;
import com.finalpos.POSsystem.Repository.UserRepository;
import com.finalpos.POSsystem.Service.Interface.UserInterface;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

import static com.finalpos.POSsystem.Service.AccountService.JWT_Key;
@Service
public class UserService implements UserInterface {
    @Autowired
    UserRepository db;
    @Value("${default.application.avatar}")
    private String defaultAvatar;

    PasswordEncoder passwordEndcoder = new BCryptPasswordEncoder();

    @Value("${default.application.SERVER_ADDRESS}")
    private String SERVER_ADDRESS;


    UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public Map<String, Object> users(int page, int size) {
        try{
            int skipAmount = (page - 1) * size;
            int totalUsers = (int) db.count();
            int totalPages = (int) Math.ceil((double) totalUsers / size);


            List<UserEntity> userList = db.findAll();
            List<UserDTO> user = new ArrayList<>();

            int endIdx = Math.min(skipAmount + size, userList.size());
            for (int i = skipAmount; i < endIdx; i++) {
                user.add(mapper.toDTO(userList.get(i)));
            }

            Map<String, Object> data = new HashMap<>();
            data.put("users", user);
            data.put("divider", totalPages);

            return data;
        } catch (Exception e) {
            throw new FailedException("Error in user service: " + e.getMessage());
        }
    }

    @Override
    public UserDTO register(String name, String email) {
        try{
            if (db.findByEmail(email) != null)
                throw new FailedException("Email already exist!");
            String username = email.split("@")[0];

            UserEntity newUser = new UserEntity();
            newUser.setName(name);
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setRole("Sale person");
            newUser.setImage(defaultAvatar);
            newUser.setStatus("InActive");
            newUser.setCreated_at(java.time.LocalDateTime.now());

            String defaultPassword = username;

            newUser.setPassword(passwordEndcoder.encode(defaultPassword));

            String tokenString = generateToken(newUser);

            sendRegistrationEmail(newUser, tokenString);
            db.save(newUser);

            return mapper.toDTO(newUser);
        } catch (Exception e) {
            throw new FailedException("Error in user service: " + e.getMessage());
        }
    }

    @Override
    public String resendEmail(String email) {
        try{
            UserEntity user = db.findByEmail(email);
            if (user == null)
                throw new FailedException("User not found!");
            user.setStatus("InActive");
            String token = generateToken(user);

            sendRegistrationEmail(user, token);
            db.save(user);
            return "Success";
        } catch (Exception e) {
            throw new FailedException("Error in user service: " + e.getMessage());
        }
    }

    @Override
    public UserDTO update(String userId, String email, String role, String status) {
        try{
            // Status: Active, InActive, Lock
            // Role: Administrator, Sale Person
            UserEntity userModel = db.findUserModelById(userId);
            userModel.setEmail(email);
            userModel.setRole(role);
            userModel.setStatus(status);
            UserEntity updateUser = db.save(userModel);
            return mapper.toDTO(updateUser);
        } catch (Exception e) {
            throw new FailedException("Error user service: " + e.getMessage());
        }
    }

    @Override
    public UserDTO delete(String userId) {
        try{
            UserEntity removeUser = db.removeUserModelById(userId);
            return mapper.toDTO(removeUser);
        } catch (Exception e) {
            throw new FailedException("Error in user service: " + e.getMessage());
        }
    }

    private String generateToken(UserEntity user) {
        Date expirationTime = new Date(System.currentTimeMillis() + 60000);
        return Jwts.builder()
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

    private void sendRegistrationEmail(UserEntity user, String token) {
        try {
            String stringSenderEmail = "vate202@gmail.com";
            String stringReceiverEmail = user.getEmail();
            String stringPasswordSenderEmail = "lktyqjjjbiyefldc";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.ssl.enable", "false");
            properties.put("mail.smtp.auth", "false");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Subject: Java App email");
            mimeMessage.setText("Hello " + user.getName() + ",\n\nYour registration was successful. Welcome to the Programmer World!"
                    + "\n\nPlease click the link below to activate your account:\n"
                    + SERVER_ADDRESS+"direct/?token=" + token
                    + "\n\nThank you,\nJava App Team");

            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
