package ua.edu.viti.military.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO for JWT authentication response.
 */
@Data
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private List<String> roles;
    
    public JwtResponseDto(String token, String username, String email, List<String> roles) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
