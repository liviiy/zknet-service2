package com.zknet.message;

import com.zknet.message.flowable.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Collection;
import java.util.List;

@Slf4j
@MapperScan("com.zknet.engine.mapper")
@SpringBootApplication(scanBasePackages = "com.zknet")
public class MessageApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MessageApplication.class, args);

        // start  listener
        run.getBeansOfType(AbstractEventListener.class).values()
                .stream()
                .forEach(AbstractEventListener::init);
    }
}
