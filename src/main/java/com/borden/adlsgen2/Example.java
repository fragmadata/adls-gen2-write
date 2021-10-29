package com.borden.adlsgen2;

import com.azure.storage.file.datalake.DataLakeDirectoryClient;
import com.azure.storage.file.datalake.DataLakeFileClient;
import com.azure.storage.file.datalake.DataLakeFileSystemClient;
import com.azure.storage.file.datalake.DataLakeServiceClient;
import com.azure.storage.file.datalake.models.FileSystemItem;
import com.borden.adlsgen2.config.ADLSUtilsAppProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static com.borden.adlsgen2.ADLSUtils.GetDataLakeServiceClient;
import static com.borden.adlsgen2.ADLSUtils.createDirectories;

@Slf4j
@ComponentScan(basePackages = {"com.borden"})
@EnableAutoConfiguration
@EnableConfigurationProperties
public class Example implements CommandLineRunner {
    @Autowired
    private ADLSUtilsAppProperties adlsUtilsAppProperties;

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }

    public void run(String... args) throws IOException {
        String accountName = adlsUtilsAppProperties.getAccountName();
        String accountKey = adlsUtilsAppProperties.getAccountKey();
        String containerName = adlsUtilsAppProperties.getContainerName();
        String pathToDumpFile = adlsUtilsAppProperties.getPathToDumpFile();
        String triggerFile = adlsUtilsAppProperties.getTriggerFile();

        final DataLakeServiceClient dataLakeServiceClient = GetDataLakeServiceClient(accountName, accountKey);

        log.info("Created ADLS client...");
        log.info("Trying to find out: " + containerName);

        final DataLakeFileSystemClient fsClient;
        final Optional<FileSystemItem> first = dataLakeServiceClient.listFileSystems().stream().filter(fileSystemItem -> fileSystemItem.getName().equals(containerName)).findFirst();
        if (first.isPresent()) {
            log.info("File system present - getting instance: " + containerName);
            fsClient = dataLakeServiceClient.getFileSystemClient(containerName);
            log.info("File system present - got instance: " + containerName);
        } else {
            final String message = "Unable to get the File System Name:" + containerName;
            log.info(message);
            throw new RuntimeException(message);
        }
        final DataLakeDirectoryClient adlsDirectory = createDirectories(fsClient, pathToDumpFile);

        final DataLakeFileClient adlsFile = adlsDirectory.createFile(triggerFile);
        final File triggerFileToUpload = createFile(triggerFile);
        adlsFile.uploadFromFile(triggerFileToUpload.getPath(), true);
        log.info("File Uploaded.");
    }

    public static File createFile(String triggerFile) throws IOException {
        final File actualFile = File.createTempFile("temp", triggerFile);
        try (FileWriter fw = new FileWriter(actualFile)) {
            fw.append("Test File Dumped at : " + new Date());
        }
        return actualFile;
    }
}
