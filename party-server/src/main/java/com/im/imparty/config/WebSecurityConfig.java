package com.im.imparty.config;

import com.im.imparty.config.security.JwtVerifyFilter;
import com.im.imparty.config.security.MyAuthenticationFilter;
import com.im.imparty.config.security.provider.JwtAuthenticationProvider;
import com.im.imparty.config.security.provider.MyAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;

@Configuration
@EnableWebSecurity//开启Spring Security的功能
//prePostEnabled属性决定Spring Security在接口前注解是否可用@PreAuthorize,@PostAuthorize等注解,设置为true,会拦截加了这些注解的接口
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationSuccessHandler loginSuccessHandler; //认证成功结果处理器
    @Resource
    private AuthenticationFailureHandler loginFailureHandler; //认证失败结果处理器

    @Resource
    private MyAuthenticationProvider myAuthenticationProvider;

    @Resource
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    //http请求拦截配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http//1、配置权限认证
                .authorizeRequests()
                .antMatchers(
                        //"/doc.html",
                        //"/swagger-ui/**",
                        "/swagger/**",
                        //"/swagger-resources/**",
                        //"/**/v3/api-docs",
                        "/**/*.js",
                        "/**/*.css",
                        "/**/*.png",
                        "/**/*.ico",
                        //"/webjars/springfox-swagger-ui/**",
                        //"/actuator/**",
                        //"/druid/**",
                        "/user/login",
                        "/user/register",
                        "/user/info",
                        "/user/logout"
                ).permitAll()
                .anyRequest() //任何其它请求
                .authenticated() //都需要身份认证
                .and()
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and()
                .authenticationProvider(jwtAuthenticationProvider)
                .authenticationProvider(myAuthenticationProvider)
                //2、登录配置表单认证方式
                //4、session管理
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .and()
                //5、禁用跨站csrf攻击防御
                .csrf()
                .disable()
                .addFilterBefore(getFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(getFilter2(), MyAuthenticationFilter.class);
        System.out.println("");

    }

    protected Filter getFilter() throws Exception {
        MyAuthenticationFilter myAuthenticationFilter = new MyAuthenticationFilter(super.authenticationManagerBean());
        myAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        myAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);
        return myAuthenticationFilter;
    }
    protected Filter getFilter2() throws Exception {
        JwtVerifyFilter myAuthenticationFilter = new JwtVerifyFilter(super.authenticationManagerBean());
        return myAuthenticationFilter;
    }

    /**
     * 指定加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        // 使用BCrypt加密密码
        return new BCryptPasswordEncoder();
    }
}
