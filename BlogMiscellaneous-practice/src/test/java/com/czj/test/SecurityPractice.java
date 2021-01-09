package com.czj.test;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class SecurityPractice {
    @Test
    public void defaultSecurity() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println(passwordEncoder);
    }

    @Test
    public void customSecurity() {
        String idForEncode = "bcrypt";
        Map encoders = new HashMap();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbdkf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("sha256", new StandardPasswordEncoder());

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    public void passwordEncode() {
        UserDetails user = User.withDefaultPasswordEncoder().password("123456").username("admin").roles("user").build();
        System.out.println(user.getPassword());
    }

    @Test
    public void BcryptPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "helloword";
        String helloword = encoder.encode(password);
        System.out.println(helloword);

        System.out.println(encoder.matches(password, helloword));
        Assert.assertTrue(encoder.matches(password,helloword));
    }
}

