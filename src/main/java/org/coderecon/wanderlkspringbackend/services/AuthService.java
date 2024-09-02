package org.coderecon.wanderlkspringbackend.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.coderecon.wanderlkspringbackend.config.JwtGenerator;
import org.coderecon.wanderlkspringbackend.dto.auth.AuthLoginDTO;
import org.coderecon.wanderlkspringbackend.models.User;
import org.coderecon.wanderlkspringbackend.repositories.UserRepository;
import org.coderecon.wanderlkspringbackend.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.NotActiveException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private Environment env;

    public ResponseEntity<Object> create(User user) {
        try {
            User userExists = userRepository.findByEmail(user.getEmail());
//           check if the user exists
            if(userExists != null){
                return Response.generate("user already exists", HttpStatus.CONFLICT);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);

            return Response.generate("user registration successful", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Error("failed to register user");
        }
    }

//    public ResponseEntity<Object> createSuperAdmin() {
//        try {
//            Optional<User> userExists = userRepository.findByEmail(env.getProperty("env.super.email"));
////           check if the user exists
//            if(userExists.isPresent()){
//                return Response.generate("user already exists", HttpStatus.CONFLICT);
//            }
//
//            Institute institute = new Institute();
//            institute.setName(env.getProperty("env.super.institute"));
//            instituteRepository.save(institute);
//
//            User user = new User();
//            user.setFirstName(env.getProperty("env.super.first_name"));
//            user.setLastName(env.getProperty("env.super.last_name"));
//            user.setEmail(env.getProperty("env.super.email"));
//            user.setRole(User.RoleTypes.SUPER_ADMIN);
//            user.setPassword(passwordEncoder.encode(env.getProperty("env.super.password")));
//            userRepository.save(user);
//
//            UserInstitute userInstitute = new UserInstitute();
//            userInstitute.setInstituteId(institute.getId());
//            userInstitute.setUserId(user.getId());
//            userInstitute.setRole(UserInstitute.RoleTypes.INSTITUTE);
//            userRepository.save(userInstitute);
//
//            return Response.generate("super admin creation successful", HttpStatus.CREATED);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new Error("failed to create super admin");
//        }
//    }

//    public ResponseEntity<Object> login(AuthLoginDTO request, HttpServletResponse response) throws UsernameNotFoundException, NotActiveException {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()));
//
//        User userResult = userRepository.findById(request.getEmail()).orElseThrow();
//        if(userResult == null){
//            throw new UsernameNotFoundException("user is not registered to the system");
//        }
//
////        generate jwt tokens for the user
//        Map<String, String> data = this.tokenAuthorizeUser(userResult);
//
////        set refresh token cookie
//        Cookie refreshCookie = new Cookie("refreshToken", data.get("refresh_token"));
//        refreshCookie.setHttpOnly(true);
//        response.addCookie(refreshCookie);
//
////        remove refresh token
//        data.remove("refresh_token");
//
//        return Response.generate("login successful", HttpStatus.OK, data);
//    }

//    public ResponseEntity<Object> invitationDetails(String token) {
//        Optional<UserInvitation> userInvitationResult =  userInvitationRepository.findByTokenAndIsValid(token, true);
//        if(userInvitationResult.isEmpty()){
//            return Response.generate("inviation token not found", HttpStatus.NOT_FOUND);
//        }
//        UserInvitation userInvitation = userInvitationResult.get();
//
//        Map<String, String> data = new HashMap<>();
//        data.put("id", userInvitation.getId().toString());
//        data.put("email", userInvitation.getUser().getEmail());
//        data.put("institute_name", userInvitation.getInstitute().getName());
//
//        return Response.generate("invitation details found", HttpStatus.OK, data);
//    }
//
//    @Transactional
//    public ResponseEntity<Object> invitationLogin(AuthInvitationAcceptDTO request, HttpServletResponse response) throws UsernameNotFoundException, NotActiveException {
////        check if a valid user invitation is found
//        Optional<UserInvitation> userInvitationResult =  userInvitationRepository.findByTokenAndIsValid(request.getToken(), true);
//        if(userInvitationResult.isEmpty()){
//            return Response.generate("inviation token not found", HttpStatus.NOT_FOUND);
//        }
//        UserInvitation userInvitation = userInvitationResult.get();
//
////        update invitation validity
//        userInvitation.setIsValid(false);
//        userInvitationRepository.save(userInvitation);
//
////        fetch user
//        Optional<User> userResult = userRepository.findById(userInvitation.getUserId());
//        if(userResult.isEmpty()) {
//            return Response.generate("user not found", HttpStatus.NOT_FOUND);
//        }
//        User user = userResult.get();
//
////        if it's a new user reset password
//        if(request.getReset()) {
//            user.setPassword(passwordEncoder.encode(request.getPassword()));
//            userRepository.save(user);
//        }
//
//        Optional<UserInstitute> userInstituteResult = userRepository.findById(userInvitation.getUserInstituteId());
//        if(userInstituteResult.isEmpty()){
//            throw new UsernameNotFoundException("user is not registered to the institute");
//        }
//        UserInstitute userInstitute = userInstituteResult.get();
//
////        update user institute active state
//        userInstitute.setIsActive(true);
//        userRepository.save(userInstitute);
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userInvitation.getUserInstituteId(),
//                        request.getPassword()));
//
////        generate jwt tokens for the user
//        Map<String, String> data = this.tokenAuthorizeUser(userInstitute);
//
////        set refresh token cookie
//        Cookie refreshCookie = new Cookie("refreshToken", data.get("refresh_token"));
//        refreshCookie.setHttpOnly(true);
//        response.addCookie(refreshCookie);
//
////        remove refresh token
//        data.remove("refresh_token");
//
//        return Response.generate("login successful", HttpStatus.OK, data);
//    }
//
//    public ResponseEntity<Object> refresh(HttpServletRequest request, HttpServletResponse response) throws UsernameNotFoundException, NotActiveException {
//        String refreshToken = jwtGenerator.extractFromCookies(request.getCookies(), "refreshToken");
//
//        if(refreshToken == null){
//            return Response.generate("refresh token is empty", HttpStatus.BAD_REQUEST);
//        }
//        String id = jwtGenerator.extractId(refreshToken);
//
//        Optional<UserInstitute> userInstitute = userRepository.findById(Long.valueOf(id));
//        if(userInstitute.isEmpty()){
//            throw new UsernameNotFoundException("user is not registered to the institute");
//        }
////        is user is not active
//        if(!userInstitute.get().getIsActive()){
//            throw new NotActiveException("user account is not active");
//        }
//
//        var accessToken = jwtGenerator.generateAccessToken(userInstitute.get());
//
//        Map<String,String> data = new HashMap<>();
//        data.put("access_token", accessToken);
//
//        return Response.generate("access token refreshed", HttpStatus.OK, data);
//    }
//
//    public ResponseEntity<Object> authorize(Long id){
//        Optional<UserInstitute> userInstitute = userRepository.findById(id);
//        if(userInstitute.isEmpty()){
//            throw new UsernameNotFoundException("user is not registered to the institute");
//        }
//
//        Map<String,String> data = this.prepareUserData(userInstitute.get());
//
//        return Response.generate("user institute details found", HttpStatus.OK, data);
//    }
//
//    public ResponseEntity<Object> institutes(String email) {
//        Optional<User> user = userRepository.findByEmail(email);
////        check if the user exists
//        if(user.isEmpty()){
//            return Response.generate("user not registered to the system", HttpStatus.NOT_FOUND);
//        }
////        get user registered institutes
//        List<UserInstitute> userInstitutes = userRepository.findByUserEmail(email);
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("institutes", userInstitutes.stream().map(src -> {
//            Map<String, Object> inner = new HashMap<>();
//            inner.put("id", src.getId());
//            inner.put("instituteId", src.getInstituteId());
//            inner.put("name", src.getInstitute().getName());
//
//            return inner;
//        }));
//
//        return Response.generate("success", HttpStatus.OK, data);
//    }
//
//    private Map<String, String> tokenAuthorizeUser(User userInstitute) {
//        var accessToken = jwtGenerator.generateAccessToken(userInstitute);
//        var refreshToken = jwtGenerator.generateRefreshToken(userInstitute);
//
//        Map<String,String> data = this.prepareUserData(userInstitute);
//        data.put("access_token", accessToken);
//        data.put("refresh_token", refreshToken);
//
//        return data;
//    }
//
//    private Map<String, String> prepareUserData(UserInstitute userInstitute) {
//        Map<String,String> data = new HashMap<>();
//        data.put("user_institute_id", userInstitute.getId().toString());
//        data.put("user_id", userInstitute.getUser().getId().toString());
//        data.put("institute_id", userInstitute.getInstitute().getId().toString());
//        data.put("first_name", userInstitute.getUser().getFirstName());
//        data.put("last_name", userInstitute.getUser().getLastName());
//        data.put("email", userInstitute.getUser().getEmail());
//        data.put("user_role", userInstitute.getUser().getRole().toString());
//        data.put("institute_role", userInstitute.getRole().toString());
//        data.put("image", userInstitute.getUser().getImage());
//
//        return data;
//    }
}
