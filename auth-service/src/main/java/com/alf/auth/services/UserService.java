package com.alf.auth.services;


import com.alf.auth.keystore.config.AuthProperties;
import com.alf.auth.keystore.service.SigningService;
import com.alf.auth.models.*;
import com.alf.auth.repo.*;
import com.alf.core_common.dtos.user.*;
import com.alf.core_common.enums.AuthorityName;
import com.alf.security.common.exceptions.ApplicationException;
import com.alf.auth.mappers.UserMapper;
import com.alf.auth.security.CustomAuthenticationImpl;
import com.alf.auth.config.DateTimeExpiredChecker;
import com.alf.security.common.jwt.JwtVerifierService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final OTPLogsRepository otpLogsRepository;
    private final ResetPasswordOtpLogsRepository resetPasswordOtpLogsRepository;

    private final UserLogsRepository userLogsRepository;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtVerifierService jwtService;
    private final DateTimeExpiredChecker dateTimeExpiredChecker;
    private final SigningService signingService;
    private final AuthProperties authProperties;

    private final OutboxRepository outboxRepo;

    private final ObjectMapper objectMapper;


    //private final JwtBlacklist jwtBlacklist;
    //private final TokenParser tokenParser;

    @Transactional
    public Map<String, String> login(String login, String password) throws JOSEException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        String accessToken =  signingService.generateAccessToken( user.getLogin(),
                Map.of("role", roles, "userName", user.getUsername()));
        /*jwtService.generateAccessToken(
                user.getLogin(),
                Map.of("role", roles, "userName", user.getUsername())
        );*/
        String jti = UUID.randomUUID().toString() ;
        String refreshToken = signingService.generateRefreshToken(user.getLogin(),jti);


        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    @Transactional
    public Map<String, String> generatePayload(User user ,  String deviceId) throws JOSEException {

        Map<String, String> result = null ;
        try{

            List<String> roles = user.getRoles()
                    .stream()
                    .map(Role::getName)
                    .toList();

            Map claims = new HashMap<>();
            claims.put("role", roles);
            claims.put("fullName", user.getFirstName() + " " + user.getLastName());
            claims.put("avatar",user.getUser_avatar());
            claims.put("cover",user.getUser_image());
            claims.put("hash", user.getAccountId());
            claims.put("lang", user.getS_cut());

            String accessToken =  signingService.generateAccessToken( user.getUsername(), claims);

            String jti = UUID.randomUUID().toString();

            String refreshToken = signingService.generateRefreshToken(user.getLogin(),jti);

            String refreshHash = signingService.hash(refreshToken);

            Long expiryDate = jwtService.getExpirationMillis(refreshToken);

            RefreshToken rt = new RefreshToken();
            rt.setId(jti);
            rt.setLogin(user.getLogin());
            rt.setTokenHash(refreshHash);
            rt.setExpiryDate(Instant.ofEpochMilli(expiryDate));
            rt.setDeviceId(deviceId);
            refreshRepo.save(rt);


            result = Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            );

        }catch ( RuntimeException ex ){
            System.out.println(ex.getMessage());
        }

        return result;
    }

    @Transactional
    public Map<String, String> refreshAccessToken(String refreshToken , String deviceId) throws JOSEException {
        String hash = signingService.hash(refreshToken);
        RefreshToken rt = refreshRepo.findByTokenHash(hash)
                .orElseThrow(() -> new IllegalArgumentException("invalid_refresh"));
        if (rt.isRevoked() || rt.isUsed() || rt.getExpiryDate().isBefore(Instant.now())) {
            // مراقبة هجوم احتمالى — revoke كل شيء متعلق بالمستخدم أو الجهاز إن لزم
            throw new IllegalArgumentException("refresh_token_invalid_or_reused");
        }

        // تحقق أن الـ deviceId يطابق (لو مخزن)
        if (rt.getDeviceId() != null && deviceId != null && !deviceId.equals(rt.getDeviceId())) {
            rt.setRevoked(true);
            refreshRepo.save(rt);
            throw new IllegalArgumentException("device_mismatch");
        }

        // علامة used للقديم (atomic داخل الترانزاكشن)
        rt.setUsed(true);
        refreshRepo.save(rt);

        // أنشئ refresh جديد
        String newRefreshRaw = signingService.generateRefreshToken(rt.getLogin(),UUID.randomUUID().toString());
        String newHash = signingService.hash(newRefreshRaw);
        Instant newExpiry = Instant.now().plus(Duration.ofDays(authProperties.getRefreshTtlDays()));

        RefreshToken newRt = new RefreshToken();
        newRt.setId(newRefreshRaw);
        newRt.setLogin(rt.getLogin());
        newRt.setTokenHash(newHash);
        newRt.setExpiryDate(newExpiry);
        newRt.setDeviceId(deviceId);
        refreshRepo.save(newRt);

        User user =
                userRepository.findByLogin(rt.getLogin()).
                        orElseThrow(()-> new ApplicationException("Unknown User" , HttpStatus.NOT_FOUND));

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        Map claims = new HashMap<>();
        claims.put("role", roles);
        claims.put("fullName", user.getFirstName() + " " + user.getLastName());
        claims.put("avatar",user.getUser_avatar());
        claims.put("cover",user.getUser_image());
        claims.put("hash", user.getAccountId());
        claims.put("lang", user.getS_cut());

        String access = signingService.generateAccessToken( user.getLogin(),claims);

        return Map.of(
                "accessToken", access ,
                "refreshToken", refreshToken // refresh token ثابت لحين انتهاء صلاحيته
        );
    }
    // logout per device: revoke/delete refresh tokens for device for a user
    @Transactional
    public void logout(String login, String deviceId, String refreshRaw) {
        if (refreshRaw != null) {
            String hash = signingService.hash(refreshRaw);
            Optional<RefreshToken> maybe = refreshRepo.findByTokenHash(hash);
            maybe.ifPresent(rt -> {
                rt.setRevoked(true);
                refreshRepo.save(rt);
            });
        } else if (deviceId != null) {
            // delete all tokens for that user & device
            refreshRepo.deleteByLoginAndDeviceId(login, deviceId);
        }
        // أيضا blacklist للـ access token ممكن تتم في مكان آخر
    }
/*
    public void logout(String accessToken, String refreshToken) {
        jwtService.revokeToken(accessToken);
        jwtService.revokeToken(refreshToken);
    }

    public Optional<User> validateAndGetUser(String token) {
        if (!jwtService.validateToken(token)) return Optional.empty();

        String login = jwtService.extractSubject(token);
        return userRepository.findByLogin(login);
    }

*/
/*
    public UserDto authenticate(TokenLoginDto tokenLoginDto){

        String login = jwtService.extractSubject(tokenLoginDto.token());

        User user =
                userRepository.findByLogin(login).
                        orElseThrow(()-> new AppExecption("Unknown User or Invalid token" , HttpStatus.NOT_FOUND));

        if(!jwtService.validateToken(tokenLoginDto.token())) {
            UserDto _user = userMapper.toUserDto(user);
            return _user;
        }

        throw new AppExecption("Token is expired ", HttpStatus.BAD_REQUEST);
    }
*/
    public UserDto login(CredentialDto credentialDto){

        User user =
                userRepository.findByLogin(credentialDto.login()).
                        orElseThrow(()-> new ApplicationException("Unknown User" , HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(CharBuffer.wrap(credentialDto.password())
                , user.getPassword())){

            return  userMapper.toUserDto(user);
        }

        throw new ApplicationException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public Authentication customAuthUserSecurity(CredentialDto credentialDto){

        User user =
                userRepository.findByLogin(credentialDto.login()).
                        orElseThrow(()-> new ApplicationException("Unknown User" , HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(CharBuffer.wrap(credentialDto.password())
                , user.getPassword())){

            return new CustomAuthenticationImpl(user);

        }

        throw new ApplicationException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto updatePassword(NewPasswordDto newPasswordDto){

        Optional<User> oUser =
                userRepository.findByLogin(newPasswordDto.login());
        UserDto userDto = null;

        if(oUser.isPresent()){
            User user = oUser.get();
            user.setPassword(passwordEncoder.encode(CharBuffer.wrap(newPasswordDto.password())));
            // Save User
            User savedUser = userRepository.save(user);
            userDto = userMapper.toUserDto(savedUser);
        }

        return  userDto;
    }

    public UserDto uploadImage(String id , String name , String avatarPath){

        Optional<User> oUser =
                userRepository.findByLogin(id);
        UserDto userDto = null;

        if(oUser.isPresent()){
            User user = oUser.get();
            user.setUser_avatar(avatarPath);
            // Save User
            User savedUser = userRepository.save(user);
            userDto = userMapper.toUserDto(savedUser);
        }

        return  userDto;
    }
    public UserDto updateAvatar(String id , String avatarPath){

        Optional<User> oUser =
                userRepository.findByLogin(id);
        UserDto userDto = null;

        if(oUser.isPresent()){
            User user = oUser.get();
            user.setUser_avatar(avatarPath);
            // Save User
            User savedUser = userRepository.save(user);
            userDto = userMapper.toUserDto(savedUser);
        }

        return  userDto;
    }

    public UserDto updateImage(String id , String imagePath){

        Optional<User> oUser =
                userRepository.findByLogin(id);
        UserDto userDto = null;

        if(oUser.isPresent()){
            User user = oUser.get();
            user.setUser_image(imagePath);
            // Save User
            User savedUser = userRepository.save(user);
            userDto = userMapper.toUserDto(savedUser);
        }

        return  userDto;
    }
    public UserDto register(SignUpDto signUpDto) throws JsonProcessingException {

        Optional<User> oUser =
                userRepository.findByLogin(signUpDto.login());

        if(oUser.isPresent()){
            throw new ApplicationException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        user.setIsOtpRequired(true);
        // set account for user
        String accountId = UUID.randomUUID().toString() ;
        user.setAccountId(accountId);

        // set Authority User By Default
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthorityName.ROLE_USER);
        authoritySet.add(authority);
        user.setAuthorityNames(authoritySet);
        // Set Role For User Authority
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setId(1);
        roles.add(role);
        user.setRoles(roles);

        User savedUser = null;

        try{
            // Save User
             savedUser = userRepository.save(user);

        }catch (RuntimeException ex){

            throw ex;

        }

        // 2️⃣ حفظ Outbox Event
        UserRegisteredEvent event = new UserRegisteredEvent( accountId ,
                user.getLogin(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail()
        );

        OutboxEvent outbox = new OutboxEvent();
        outbox.setId(accountId);
        outbox.setAggregateId(user.getLogin());
        outbox.setType("UserRegisteredEvent");
        outbox.setPayload(objectMapper.writeValueAsString(event));
        outbox.setSent(false);
        outbox.setCreatedAt(LocalDateTime.now());
        outboxRepo.save(outbox);

        return  userMapper.toUserDto(savedUser);
    }

    public UserDto register(UserSignUpByAccount userSignUpByAccount){

        Optional<User> oUser =
                userRepository.findByLogin(userSignUpByAccount.login());

        if(oUser.isPresent()){
            throw new ApplicationException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUserByAccount(userSignUpByAccount);

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userSignUpByAccount.password())));
        user.setIsOtpRequired(true);
        // set Authority User By Default
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthorityName.ROLE_USER);
        authoritySet.add(authority);
        user.setAuthorityNames(authoritySet);
        // Set Role For User Authority
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setId(1);
        roles.add(role);
        user.setRoles(roles);
        //Set Account

        user.setAccountId(userSignUpByAccount.id().toString());
        // Save User

        User savedUser = userRepository.save(user);

        return  userMapper.toUserDto(savedUser);
    }

    public UserDto registerByAccount(AccountRegister accountRegister,String accountId){

        Optional<User> oUser =
                userRepository.findByLogin(accountRegister.login());

        if(oUser.isPresent()){
            throw new ApplicationException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.accountSignUpToUser(accountRegister);

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(accountRegister.password())));
        user.setIsOtpRequired(true);
        // set Authority User By Default
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthorityName.ROLE_ADMIN);
        authoritySet.add(authority);
        user.setAuthorityNames(authoritySet);
        // Set Role For User Authority
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setId(1);
        roles.add(role);
        user.setRoles(roles);
        //Set account
        user.setAccountId(accountId);

        // Save User
        User savedUser = userRepository.save(user);
        return  userMapper.toUserDto(savedUser);
    }

    public String findEmailByLogin(String login){
        Optional<User> user = userRepository.findByLogin(login);
        return user.isPresent() ? user.orElse(null).getEmail() : ""  ;
    }
/*
    public UserLog updateUserLogs(String token, boolean rememberMe){

        String login = jwtService.extractSubject(token);

        Optional<UserLog> optionalUserLog = userLogsRepository.findById(login);

        UserLog userLog  ;

        if(optionalUserLog.isPresent()){
            userLog = optionalUserLog.get();
            if (!userLog.getLogToken().equals(token))
                userLog.setLogToken(token);
            if(userLog.isRememberMe() != rememberMe)
                userLog.setRememberMe(rememberMe);
        }
        else{
            userLog = new UserLog(login,token,rememberMe);
        }


        return  userLogsRepository.save(userLog);
    }

    public boolean isRememberMe(String token){

        boolean rememberMe = false;

        String login = jwtService.extractSubject(token);

        Optional<UserLog> optionalUserLog = userLogsRepository.findById(login);

        return optionalUserLog.isPresent() ? optionalUserLog.orElse(null).isRememberMe() : rememberMe ;
    }

    public boolean isValidToken(String token){

        boolean rememberMe = false;

        String login = jwtService.extractSubject(token);

        Optional<UserLog> optionalUserLog = userLogsRepository.findById(login);

        boolean result = optionalUserLog.isPresent() ?
                optionalUserLog.orElse(null).getLogToken().equals(token)
                : rememberMe;

        return result;
    }
*/
    public void updateOtpLog(String login,String otp){

        Optional<OtpLog> optionalOtpLog = otpLogsRepository.findById(login);
        OtpLog otpLog ;
        if(optionalOtpLog.isPresent()){
            otpLog = optionalOtpLog.get();
            otpLog.setOtpToken(otp);
            otpLog.setExpireTime(dateTimeExpiredChecker.getTimeAfterDay());
            otpLog.setOtpValid(false);
        }
        else {
            otpLog = new OtpLog(login,otp, dateTimeExpiredChecker.getTimeAfterDay(), false);
        }
        otpLogsRepository.save(otpLog);
    }

    public void updateResetPasswordOtpLog(String login,String otp){

        Optional<ResetPasswordOtpLog> optionalResetPasswordOtpLog = resetPasswordOtpLogsRepository.findById(login);
        ResetPasswordOtpLog resetPasswordOtpLog ;
        if(optionalResetPasswordOtpLog.isPresent()){
            resetPasswordOtpLog = optionalResetPasswordOtpLog.get();
            resetPasswordOtpLog.setOtpToken(otp);
            resetPasswordOtpLog.setExpireTime(dateTimeExpiredChecker.getTimeAfterDay());
            resetPasswordOtpLog.setOtpValid(false);
        }
        else {
            resetPasswordOtpLog = new ResetPasswordOtpLog(login,otp, dateTimeExpiredChecker.getTimeAfterDay(), false);
        }
        resetPasswordOtpLogsRepository.save(resetPasswordOtpLog);
    }

    public UserDto validOTP(String login,String otp){

        User user =
                userRepository.findByLogin(login).
                        orElseThrow(()-> new ApplicationException("Unknown_User" , HttpStatus.NOT_FOUND));

        Optional<OtpLog> optionalOtpLog = otpLogsRepository.findById(login);
        OtpLog otpLog ;

        if(optionalOtpLog.isPresent() ){

            otpLog = optionalOtpLog.get();

            if(otpLog.isOtpValid())
                return userMapper.toUserDto(user);

            if( !dateTimeExpiredChecker.validWithCurrentDateExpired(otpLog.getExpireTime()) ) {

                if(otpLog.getOtpToken().equals(otp) ){

                    otpLog.setOtpValid(true);

                    otpLogsRepository.save(otpLog);

                }

            }
            else{
                throw new ApplicationException("OTP_Expired", HttpStatus.BAD_REQUEST);
            }
        }

        return   userMapper.toUserDto(user);
    }

    public UserDto validOTP_ResetPassword(String login,String otp){

        User user =
                userRepository.findByLogin(login).
                        orElseThrow(()-> new ApplicationException("Unknown_User" , HttpStatus.NOT_FOUND));

        Optional<ResetPasswordOtpLog> optionalResetPasswordOtpLog = resetPasswordOtpLogsRepository.findById(login);

        ResetPasswordOtpLog resetPasswordOtpLog ;

        if(optionalResetPasswordOtpLog.isPresent() ){

            resetPasswordOtpLog = optionalResetPasswordOtpLog.get();

            if(resetPasswordOtpLog.isOtpValid())
                return userMapper.toUserDto(user);

            if( !dateTimeExpiredChecker.validWithCurrentDateExpired(resetPasswordOtpLog.getExpireTime()) ) {

                if(resetPasswordOtpLog.getOtpToken().equals(otp) ){

                    resetPasswordOtpLog.setOtpValid(true);

                    resetPasswordOtpLogsRepository.save(resetPasswordOtpLog);

                }

            }
            else{
                throw new ApplicationException("OTP_Expired", HttpStatus.BAD_REQUEST);
            }
        }

        return   userMapper.toUserDto(user);
    }

    public  boolean loginIsExists(String login){
        Optional<User> optionalUser = userRepository.findByLogin(login);
        return optionalUser.isPresent() ;
    }

    public UserDto getUserById(String id){
        Long userId = Long.valueOf(id);
        User user =
                userRepository.findById(userId).
                        orElseThrow(()-> new ApplicationException("Unknown_User" , HttpStatus.NOT_FOUND));

        return  userMapper.toUserDto(user);

    }

    public UserDto getUser(String login){
        User user =
                userRepository.findByLogin(login).
                        orElseThrow(()-> new ApplicationException("Unknown_User" , HttpStatus.NOT_FOUND));
        return  userMapper.toUserDto(user);
    }



    /*   Old Methods End   */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                userRepository.findByLogin(username).
                        orElseThrow(()-> new ApplicationException("Unknown_User" , HttpStatus.NOT_FOUND));

        return user;
    }
}
