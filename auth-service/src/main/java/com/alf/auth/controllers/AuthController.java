package com.alf.auth.controllers;

import com.alf.auth.config.PatternValidation;
import com.alf.auth.models.User;
import com.alf.auth.services.OTPService;
import com.alf.auth.services.UserService;
import com.alf.core_common.dtos.payload.MessageResponse;
import com.alf.core_common.dtos.user.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.CharBuffer;
import java.util.Map;

@RestController
//@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService ;

    private final AuthenticationManager authenticationManager;

    private final PatternValidation patternValidation;


    private final OTPService otpService;


    /*
        private final UserMapper userMapper ;
        @Autowired
        private OTPService otpService;
    */
    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@RequestBody CredentialDto credentialDto){

        int status = 200;

        String message = "ok";

        Map<String, String> res = null;

        try {
            // log in user
           var usernamePassword = new UsernamePasswordAuthenticationToken(credentialDto.login(), CharBuffer.wrap(credentialDto.password()));

            var authUser = authenticationManager.authenticate(usernamePassword);

            SecurityContext secContext =  SecurityContextHolder.getContext() ;

            secContext.setAuthentication(authUser);

            User _user = (User) authUser.getPrincipal();

            res = userService.generatePayload(_user,credentialDto.deviceId());

            //userService.updateUserLogs(res.get("refreshToken"), credentialDto.rememberMe());

            status = 200;

        } catch (RuntimeException | JOSEException ex) {

            status = 400 ;

            message = ex.getMessage();
        }

        return ResponseEntity.ok(  new MessageResponse(message,status,status,res,null)  );

    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<MessageResponse> authenticate(HttpServletRequest request, @RequestBody TokenLoginDto tokenLoginDto ){
        int status = 200;
        String message = "ok";
        Map<String, String> res = null;
        try {
            String deviceId =    request.getHeader("X-Device-Id") ; // (String) request.getAttribute("deviceId");
            res = userService.refreshAccessToken(tokenLoginDto.token() ,  deviceId);
        } catch (RuntimeException | JOSEException ex) {
            status = 400 ;
            message = ex.getMessage();
        }
        return ResponseEntity.ok(  new MessageResponse(message,status,status,res,null)  );
    }
/*
    @PostMapping("/company-register")
    public ResponseEntity<MessageResponse> accountRegister(@RequestBody AccountCompany accountCompany){

        int status = 200;

        String message = "ok";

        AccountDto accountDto = null;

        if( accountCompany.accountName() == "" || accountCompany.accountName() == null)
            return  ResponseEntity.ok(
                    new MessageResponse("Name is not found",703,703,null,null)
            );

        try {

            accountDto = accountService.updateAccount(accountCompany);

        } catch (RuntimeException ex) {

            status = 400 ;

            message = ex.getMessage();
        }

        return ResponseEntity.ok( new MessageResponse(message,status,status,accountDto,null)  );
    }
*/
    @PostMapping("/user-register")
    public ResponseEntity<MessageResponse> userRegister(@RequestBody SignUpDto signUpDto){

        int status = 200;

        String message = "ok";

        UserDto user = null;

     /*   if(!patternValidation.validEmail(signUpDto.login()) && !patternValidation.validPhone(signUpDto.login()))
            return  ResponseEntity.ok(
                    new MessageResponse("Login doesn't like email or phone",703,703,null,null)
            );*/

        try {

            if(!userService.loginIsExists(signUpDto.login())){
                // save user
                user = userService.register(signUpDto);
                // generate JWT token
                //user.setToken(userAuthProvider.createToken(user));
                // OTP Sent
                otpService.sendOTP2Email(signUpDto.login());
            }
            else{
                status = 401;
            }


        } catch (RuntimeException ex) {

            status = 400 ;

            message = ex.getMessage();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok( new MessageResponse(message,status,status,user,null)  );
    }


    @PostMapping("/forgot")
    public ResponseEntity<MessageResponse> forgot(@RequestBody LoginDto loginDto){

        int status = 200;

        String message = "ok";

        UserDto user = null;

        try {

            user = userService.getUser(loginDto.id());
            /*
            if(patternValidation.validEmail(loginDto.id())){
                otpService.sendOTP2Email_ResetPassword(loginDto.id());
            }
            else if(patternValidation.validPhone(loginDto.id()))
            {
                otpService.sendOTP2Phone_ResetPassword(loginDto.id());
            }
            */
            otpService.sendOTP2Email_ResetPassword(loginDto.id());

        } catch (RuntimeException ex) {

            status = 400 ;

            message = ex.getMessage();
        }

        return ResponseEntity.ok( new MessageResponse(message,status,status,user,null)  );
    }

    @PostMapping("/update-password")
    public ResponseEntity<MessageResponse> updatePassword(@RequestBody NewPasswordDto newPasswordDto){

        int status = 200;

        String message = "ok";

        UserDto user = null;

        try {

            // update password for user
            user = userService.updatePassword(newPasswordDto);
            if(null != user){
                // generate JWT token
                //user.setToken(userAuthProvider.createToken(user));
                // done
                status = 201;
            }

        } catch (RuntimeException ex) {

            status = 400 ;

            message = ex.getMessage();
        }
        return ResponseEntity.ok(  new MessageResponse(message,status,status,user,null)  );
    }

    @PostMapping(value = "/verify-register")
    public ResponseEntity<MessageResponse> verifyRegister(@RequestBody VerifyOTP verifyOTP){

        int status = 200;

        String message = "ok";

        UserDto user = null;


        try {

            if(!userService.loginIsExists(verifyOTP.login()))

                return  ResponseEntity.ok(
                        new MessageResponse("User not found ... ",703,703,null,null)
                );

            else{

                user =  userService.validOTP(verifyOTP.login(), verifyOTP.otp());

                if(null != user) {
                //    user.setToken(userAuthProvider.createToken(user));
                }
                else
                    status = 201;

            }


        } catch (RuntimeException ex) {

            status = 400 ;

            message = ex.getMessage();
        }

        return ResponseEntity.ok( new MessageResponse(message,status,status,user,null)  );
    }

    @PostMapping(value = "/new-otp")
    public ResponseEntity<MessageResponse> CreateNewOtp(@RequestBody LoginDto loginDto){

        int status = 200;

        String message = "ok";

        UserDto user = null;


        try {

            if(!userService.loginIsExists(loginDto.id()))

                return  ResponseEntity.ok(
                        new MessageResponse("User not found ... ",703,703,null,null)
                );

            else{
                otpService.sendOTP2Email(loginDto.id());
            }


        } catch (RuntimeException ex) {

            status = 400 ;

            message = ex.getMessage();
        }

        return ResponseEntity.ok( new MessageResponse(message,status,status,user,null)  );
    }

    @PostMapping(value = "/verify-reset-password")
    public ResponseEntity<MessageResponse> verifyresetpassword(@RequestBody VerifyOTP verifyOTP){

        int status = 200;

        String message = "ok";

        UserDto user = null;


        try {

            if(!userService.loginIsExists(verifyOTP.login()))

                return  ResponseEntity.ok(
                        new MessageResponse("User not found ... ",703,703,null,null)
                );

            else{

                user =  userService.validOTP_ResetPassword(verifyOTP.login(), verifyOTP.otp());

                if(null != user) {

                  //  user.setToken(userAuthProvider.createToken(user));

                }
                else
                    status = 201;
            }


        } catch (RuntimeException ex) {

            status = 400 ;

            message = ex.getMessage();
        }

        return ResponseEntity.ok( new MessageResponse(message,status,status,user,null)  );
    }



    @GetMapping("/user-info")
    public ResponseEntity<MessageResponse> getUser(@RequestParam String login) {
        UserDto res = userService.getUser(login);
        return ResponseEntity.ok(  new MessageResponse("OK",200,200,res,null)  );
    }


    @GetMapping("/test")
    public String test() {
        return "Auth Service is reachable via Gateway ....... ✅";
    }

    @GetMapping("/ping")
    public String ping() {
        return "✅ Auth-Service is reachable _____.✅";
    }

}
