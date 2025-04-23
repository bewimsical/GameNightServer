package edu.farmingdale.getgame;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AzureStorageConfig {
    @Value("${azure.storage.connection.string}")
    private String connectionString;
    
    @Getter
    @Value("${azure.storage.container.name}")
    private String containerName;
    
    @Getter
    @Value("${azure.mysql.connection.string}")
    private String mysqlConnectionString;
    
    @Getter
    @Value("${azure.mysql.username}")
    private String mysqlUsername;
    
    @Getter
    @Value("${azure.mysql.password}")
    private String mysqlPassword;

    // Getters
    public String getStorageConnectionString() { return connectionString; }

}