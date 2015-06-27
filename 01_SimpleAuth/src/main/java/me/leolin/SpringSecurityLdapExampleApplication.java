package me.leolin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringSecurityLdapExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityLdapExampleApplication.class, args);
    }

    @RequestMapping
    public Authentication hello() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    @Configuration
    static class LdapConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().and().authorizeRequests().anyRequest().authenticated()
                    .and().csrf().disable();

        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.ldapAuthentication()
                    .contextSource().url("ldap://localhost:10389/dc=leolin,dc=me")
                    .managerDn("uid=admin,ou=system").managerPassword("secret")
                    .and()
                    .userSearchBase("ou=users")
                    .userSearchFilter("(cn={0})");
        }
    }

}
