package com.example.simpleblog.Users;

import com.example.simpleblog.Registration.Token.ConfirmationToken;
import com.example.simpleblog.Registration.Token.ConfirmationTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User not found:", email)));
    }

    public String signUpUser(User user) {
        boolean UserExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if(UserExists){
            throw new IllegalStateException("Користувач з таким email вже існує");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

//      TODO: SEND EMAIL

        return token;
    }
    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }
}
