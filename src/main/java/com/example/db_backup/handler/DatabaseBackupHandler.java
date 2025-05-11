package com.example.db_backup.handler;

public interface DatabaseBackupHandler {
    String backup(String host, int port, String dbName, String user, String pass, String outputDir);
    String restore(String host, int port, String dbName, String user, String pass, String inputDir);
    String getType(); // Returns "POSTGRES" or "MONGO"
}