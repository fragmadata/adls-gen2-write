package com.fragma.adlsgen2.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@ConfigurationProperties(prefix="adlsapp")
@Getter
@Setter
@ToString
public class ADLSUtilsAppProperties {
    private String accountName;
    private String accountKey;
    private String containerName;
    private String pathToDumpFile;
    private String triggerFile;

    @PostConstruct
    public void printConfigs() {
        log.info("Printing ADLSUtilsAppProperties configs: {}", this);
    }
}
