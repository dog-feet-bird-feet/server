package com.capstone.dfbf.api.member;

import com.capstone.dfbf.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String encodedPassword;

//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    public enum Role {
//        USER("USER"),;
//
//        Role(String type) {}
//
//        private String type;
//    }

    public static Member create(String email, String encodedPassword){
        return Member.builder()
                .email(email)
                .encodedPassword(encodedPassword)
                .build();
    }

    public boolean isPasswordMatch(PasswordEncoder passwordEncoder, String rawPassword) {
        return passwordEncoder.matches(rawPassword, this.encodedPassword);
    }
}
