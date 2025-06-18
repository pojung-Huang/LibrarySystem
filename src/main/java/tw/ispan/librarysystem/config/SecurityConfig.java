package tw.ispan.librarysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // ⚠️ 暫時關閉 CSRF（方便 POST 測試）
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/members/register").permitAll() // ✅ 開放註冊
                        .anyRequest().permitAll() // 👉 暫時開放全部，如需安全可改 authenticated()
                );
        return http.build();
    }
}
