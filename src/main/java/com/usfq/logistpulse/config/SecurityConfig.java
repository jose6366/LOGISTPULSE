package com.usfq.logistpulse.config;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean UserDetailsService users(PasswordEncoder e){return new InMemoryUserDetailsManager(
   User.withUsername("operator").password(e.encode("operator123")).roles("OPERATOR").build(),
   User.withUsername("supervisor").password(e.encode("supervisor123")).roles("SUPERVISOR").build());}
 @Bean SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
   http.authorizeHttpRequests(a->a.requestMatchers("/health","/actuator/health","/css/**","/error").permitAll().anyRequest().authenticated())
   .csrf(csrf -> csrf.disable())
   .httpBasic(Customizer.withDefaults())
   .formLogin(f->f.defaultSuccessUrl("/",true)).logout(l->l.logoutSuccessUrl("/login?logout")); return http.build();
 }
}
