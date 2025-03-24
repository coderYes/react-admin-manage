package com.illusiontour.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.SpringVersion;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
@ComponentScan(basePackages = {"com.illusiontour.admin", "com.illusiontour.common", "com.illusiontour.framework", "com.illusiontour.system"})
public class IllusionApplication {
    public static void main(String[] args) {
        String sVersion = SpringVersion.getVersion();
        System.out.println("Spring 版本号：" + sVersion);
        String bVersion = SpringBootVersion.getVersion();
        System.out.println("SpringBoot 版本号：" + bVersion);
        SpringApplication.run(IllusionApplication.class, args);
    }
}
