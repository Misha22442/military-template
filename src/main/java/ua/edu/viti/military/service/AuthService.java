package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.LoginRequestDto;
import ua.edu.viti.military.dto.request.RegisterRequestDto;
import ua.edu.viti.military.dto.response.JwtResponseDto;
import ua.edu.viti.military.entity.Role;
import ua.edu.viti.military.entity.RoleName;
import ua.edu.viti.military.entity.User;
import ua.edu.viti.military.exception.DuplicateResourceException;
import ua.edu.viti.military.exception.ResourceNotFoundException;
import ua.edu.viti.military.repository.RoleRepository;
import ua.edu.viti.military.repository.UserRepository;
import ua.edu.viti.military.security.JwtUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service for authentication and user registration operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    
    /**
     * Authenticate user and generate JWT token.
     */
    public JwtResponseDto login(LoginRequestDto dto) {
        log.info("User login attempt: {}", dto.getUsername());
        
        // 1. Authenticate
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword()
            )
        );
        
        // 2. Set in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 3. Generate JWT token
        String jwt = jwtUtils.generateToken(authentication);
        
        // 4. Get UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        
        // 5. Get user email
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("Користувача не знайдено"));
        
        log.info("User logged in successfully: {}", dto.getUsername());
        
        return new JwtResponseDto(
            jwt,
            userDetails.getUsername(),
            user.getEmail(),
            roles
        );
    }
    
    /**
     * Register new user.
     */
    @Transactional
    public String register(RegisterRequestDto dto) {
        log.info("User registration attempt: {}", dto.getUsername());
        
        // 1. Check if user exists
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username вже зайнятий");
        }
        
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email вже зареєстрований");
        }
        
        // 2. Create User
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));  // Hash password
        user.setFullName(dto.getFullName());
        user.setMilitaryRank(dto.getMilitaryRank());
        user.setEnabled(true);
        
        // 3. Assign roles
        Set<Role> roles = new HashSet<>();
        
        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            // Default role - VIEWER
            Role viewerRole = roleRepository.findByName(RoleName.ROLE_VIEWER)
                .orElseThrow(() -> new ResourceNotFoundException("Роль VIEWER не знайдено"));
            roles.add(viewerRole);
        } else {
            // Assign specified roles
            for (String roleName : dto.getRoles()) {
                try {
                    RoleName roleEnum = RoleName.valueOf(roleName);
                    Role role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new ResourceNotFoundException("Роль не знайдено: " + roleName));
                    roles.add(role);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid role name: {}", roleName);
                }
            }
        }
        
        user.setRoles(roles);
        
        // 4. Save
        userRepository.save(user);
        
        log.info("User registered successfully: {}", dto.getUsername());
        
        return "Користувача зареєстровано успішно";
    }
}
