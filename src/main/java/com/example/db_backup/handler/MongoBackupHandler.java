package com.example.db_backup.handler;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MongoBackupHandler implements DatabaseBackupHandler {
    @Override
    public String backup(String host, int port, String dbName, String user, String pass, String outputDir) {
        return "Mongo Backup needs to be implemented";
    }
    @Override
    public String restore(String host, int port, String dbName, String user, String pass, String inputDir){
        return "Mongo Restore needs to be implemented";
    }
    @Override
    public String getType() {
        return "MONGO";
    }
}
