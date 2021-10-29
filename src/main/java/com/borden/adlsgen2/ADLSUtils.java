package com.borden.adlsgen2;

import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.file.datalake.DataLakeDirectoryClient;
import com.azure.storage.file.datalake.DataLakeFileSystemClient;
import com.azure.storage.file.datalake.DataLakeServiceClient;
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder;
import org.apache.commons.lang3.tuple.Pair;
public class ADLSUtils {
    static public DataLakeServiceClient GetDataLakeServiceClient
            (String accountName, String accountKey){

        StorageSharedKeyCredential sharedKeyCredential =
                new StorageSharedKeyCredential(accountName, accountKey);

        DataLakeServiceClientBuilder builder = new DataLakeServiceClientBuilder();

        builder.credential(sharedKeyCredential);
        builder.endpoint("https://" + accountName + ".dfs.core.windows.net");

        return builder.buildClient();
    }

    public static DataLakeDirectoryClient createDirectories(DataLakeFileSystemClient fsClient, String pathInBlob) {
        final String[] directories = pathInBlob.split("/");

        DataLakeDirectoryClient currentPath = null;
        for (String directory : directories) {
            if(currentPath == null) {
                currentPath = fsClient.getDirectoryClient(directory);
                if(!currentPath.exists()) {
                    currentPath.create();
                }
            } else {
                currentPath = currentPath.getSubdirectoryClient(directory);
                if(!currentPath.exists()) {
                    currentPath.create();
                }
            }
        }
        return currentPath;
    }

    public static Pair<String, String> containerAndPathPair(String pathWithContainer){
        final int firstPartIndex = pathWithContainer.indexOf('/');
        final String fileSystemName = pathWithContainer.substring(0, firstPartIndex);
        String pathToDumpFiles = pathWithContainer.substring(firstPartIndex+1);
        return Pair.of(fileSystemName, pathToDumpFiles);
    }
}
