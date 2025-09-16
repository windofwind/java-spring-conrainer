package info.addline.acution.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정을 담당하는 구성 클래스입니다.
 *
 * <p>이 클래스는 애플리케이션의 보안 설정을 정의하며,
 * 비밀번호 암호화 및 HTTP 보안 필터 체인을 구성합니다.</p>
 *
 * <p>주요 기능:</p>
 * <ul>
 *   <li>BCryptPasswordEncoder를 이용한 비밀번호 암호화</li>
 *   <li>HTTP 보안 필터 체인 설정</li>
 *   <li>API 엔드포인트에 대한 접근 권한 설정</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈을 생성합니다.
     *
     * <p>BCryptPasswordEncoder를 사용하여 안전한 비밀번호 해싱을 제공합니다.</p>
     *
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HTTP 보안 필터 체인을 구성합니다.
     *
     * <p>현재는 모든 요청에 대해 접근을 허용하도록 설정되어 있으며,
     * CSRF 보호 기능을 비활성화합니다. 향후 인증/인가 로직을 추가할 수 있습니다.</p>
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception 보안 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            );

        return http.build();
    }
}