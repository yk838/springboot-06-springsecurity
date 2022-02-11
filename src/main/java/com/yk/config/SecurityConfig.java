package com.yk.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author yk
 * @date 2022/1/21  18:19
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //链式编程
    //请求授权规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 首页所有人都可以访问， 功能页只有对应有权限的人可以访问
       http.authorizeRequests()
               .antMatchers("/").permitAll()
               .antMatchers("/level1/**").hasRole("vip1")
               .antMatchers("/level2/**").hasRole("vip2")
               .antMatchers("/level3/**").hasRole("vip3");

       //没有授权的用户默认会登到登录页面，需要开启登录功能
        http.formLogin().loginPage("/toLogin");
        //注销
        // 防止网站工具 ：get post
        http.csrf().disable();//关闭scrf功能
        http.logout().logoutSuccessUrl("/");

        //开启记住我功能
        http.rememberMe();

    }
    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //这些数据正常是从数据库中获取
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("kuangshen").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")
                .and()
                .withUser("root").password((new BCryptPasswordEncoder().encode("root"))).roles("vip1","vip2","vip3")
                .and()
                .withUser("guest").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1");
    }
}