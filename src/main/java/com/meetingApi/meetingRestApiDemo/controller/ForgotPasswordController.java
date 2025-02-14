package com.meetingApi.meetingRestApiDemo.controller;

import com.meetingApi.meetingRestApiDemo.businiess.abstracts.EmailService;
import com.meetingApi.meetingRestApiDemo.businiess.dto.request.ChangePasswordRequest;
import com.meetingApi.meetingRestApiDemo.businiess.mail.MailBody;
import com.meetingApi.meetingRestApiDemo.dataacess.ForgotPasswordRepository;
import com.meetingApi.meetingRestApiDemo.dataacess.UserRepository;
import com.meetingApi.meetingRestApiDemo.entities.ForgotPassword;
import com.meetingApi.meetingRestApiDemo.entities.User;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {
    private final UserRepository userRepository;
    private  final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // send mail for verification

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity verifyMail(@PathVariable String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Lürfen geçerli mail giriniz ! "));
        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("Bu şifreni değiştirme isteğin için tek kullanımlık şifren: " + otp)
                .subject("ŞİFREMİ UNUTTUM ! Tek Kullanımlık Şifren")
                .build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                        .otp(otp)
                                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                                        .user(user)
                                                .build();
        emailService.sendSimpleMail(mailBody);
        forgotPasswordRepository.save(forgotPassword);
        return  ResponseEntity.ok("Email sent for veritification");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordRequest changePasswordRequest,@PathVariable String email){
        if (!Objects.equals(changePasswordRequest.password(),changePasswordRequest.repeatPassword())){
            return new ResponseEntity<>("Please enter the password again", HttpStatus.EXPECTATION_FAILED);
        }
        String encodedPassword = passwordEncoder.encode(changePasswordRequest.password());
        userRepository.updatePassword(email,encodedPassword);
        return  ResponseEntity.ok("Password has been changed!");
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public  ResponseEntity<String> verifyOtp(@PathVariable Integer otp,@PathVariable String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new   UsernameNotFoundException("Lütfen geçerli mail giriniz"));

        ForgotPassword  fp =forgotPasswordRepository.findbyOtpAndUser(otp,user).orElseThrow(()-> new RuntimeException("Invalid OTP for mail: " +email
        ));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(fp.getFpid());
            return  new ResponseEntity<>("OTP has expired!" , HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified");

    }

    private  Integer otpGenerator(){
        Random random = new Random();
        return  random.nextInt(100_000, 999_999);

    }

}
