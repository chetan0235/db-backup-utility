package com.example.db_backup;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class DbBackupCommand {
    private final BackupService backupService;

    @ShellMethod(key = "db-backup", value = "Backup a database (MongoDB, PostgreSQL).")
    public String backupDatabase(
            @ShellOption(help = "Database type: POSTGRES or MONGO") DatabaseType type,
            @ShellOption(help = "Database host") String host,
            @ShellOption(help = "Database port") int port,
            @ShellOption(help = "Database name") String dbName,
            @ShellOption(help = "Username", defaultValue = ShellOption.NULL) String username,
            @ShellOption(help = "Password", defaultValue = ShellOption.NULL) String password,
            @ShellOption(help = "Backup output directory") String backupDir
    ) {
        return backupService.backup(type, host, port, dbName, username, password, backupDir);
    }

    @ShellMethod(key = "db-restore", value = "Restore a database (MongoDB, PostgreSQL).")
    public String restoreDatabase(
            @ShellOption(help = "Database type: POSTGRES or MONGO") DatabaseType type,
            @ShellOption(help = "Database host") String host,
            @ShellOption(help = "Database port") int port,
            @ShellOption(help = "Database name") String dbName,
            @ShellOption(help = "Username", defaultValue = ShellOption.NULL) String username,
            @ShellOption(help = "Password", defaultValue = ShellOption.NULL) String password,
            @ShellOption(help = "Backup output directory") String backupDir
    ) {
        return backupService.restore(type, host, port, dbName, username, password, backupDir);
    }
}
