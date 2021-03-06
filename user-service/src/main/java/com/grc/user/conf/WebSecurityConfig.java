package com.grc.user.conf;

import com.grc.user.jwt.JsonWebTokenSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author 郭若辰
 * @create 2017-04-26 8:46
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends JsonWebTokenSecurityConfig {

    @Override
    protected void setupAuthorization(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 此url不需要认证
                .antMatchers("/user/login").permitAll();

        //下面的加上就不对了  不过现在实际上也没加上认证

                // 其他的url都需要认证
//                .anyRequest().authenticated();
    }
}
