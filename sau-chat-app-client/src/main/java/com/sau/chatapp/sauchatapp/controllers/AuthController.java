package com.sau.chatapp.sauchatapp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sau.chatapp.sauchatapp.entities.ERole;
import com.sau.chatapp.sauchatapp.entities.Role;
import com.sau.chatapp.sauchatapp.entities.User;
import com.sau.chatapp.sauchatapp.repositories.RoleRepository;
import com.sau.chatapp.sauchatapp.repositories.UserRepository;
import com.sau.chatapp.sauchatapp.security.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		 String jwt = jwtUtils.generateJwtToken(authentication);
        Cookie cookie = new Cookie("JWT", jwt);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 saat geçerli olacak şekilde ayarlandı
        response.addCookie(cookie);
        
		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/").build();

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		if (userRepository.existsByUsername(username)) {
			String errorMessage = "Error: Username is already taken!";
			String jsCode = "alert('" + errorMessage + "')";
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<script type=\"text/javascript\">" + jsCode + "</script>");
			out.flush();
			return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/signin").build();
		}

		if (userRepository.existsByEmail(email)) {
			String errorMessage = "Error: Email is already in use!";
			String jsCode = "alert('" + errorMessage + "')";
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<script type=\"text/javascript\">" + jsCode + "</script>");
			out.flush();
			return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/signin").build();
		}

		// Create new user's account
		User user = new User(username, email, encoder.encode(password));

		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/").build();
	}
}
