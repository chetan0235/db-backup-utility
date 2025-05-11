package com.example.db_backup;

import com.example.db_backup.handler.DatabaseBackupHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackupService {

    private final List<DatabaseBackupHandler> handlers;

    public BackupService(List<DatabaseBackupHandler> handlers) {
        this.handlers = handlers;
    }

    public String backup(DatabaseType type, String host, int port, String dbName, String user, String pass, String dir) {
        for (DatabaseBackupHandler handler : handlers) {
            if (handler.getType().equalsIgnoreCase(type.name())) {
                return handler.backup(host, port, dbName, user, pass, dir);
            }
        }
        return "Unsupported database type: " + type;
    }

    public String restore(DatabaseType type, String host, int port, String dbName,String user, String pass, String dir){
        for (DatabaseBackupHandler handler : handlers) {
            if (handler.getType().equalsIgnoreCase(type.name())) {
                return handler.restore(host, port, dbName, user, pass, dir);
            }
        }
        return "Unsupported database type: " + type;
    }
}