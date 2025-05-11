package com.example.db_backup.handler;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class PostgresBackupHandler implements DatabaseBackupHandler{
    @Override
    public String backup(String host, int port, String dbName, String user, String pass, String outputDir) {
        File dir = new File(outputDir);
        if(!dir.exists()){
            boolean created = dir.mkdirs();
            if(!created){
                return "Failed to create backup directory" + outputDir;
            }
        }
        String backupFile = outputDir + "/" + dbName + "_backup.sql";
        String command = String.format(
                "pg_dump -h %s -p %d -U %s -F c -b -v -f \"%s\" %s",
                host, port, user, backupFile, dbName
        );
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.environment().put("PGPASSWORD", pass);
            pb.inheritIO();

            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0 ? "PostgreSQL backup successful: " + backupFile
                    : "PostgreSQL backup failed with exit code " + exitCode;

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "PostgreSQL backup error: " + e.getMessage();
        }
    }
    @Override
    public String restore(String host, int port, String dbName, String user, String pass, String inputFile) {
        StringBuilder output = new StringBuilder();

        try {
            String[] command = {
                    "pg_restore",
                    "-h", host,
                    "-p", String.valueOf(port),
                    "-U", user,
                    "-v",
                    "-C",
                    "-c",
                    "-d", "postgres",
                    inputFile
            };


            ProcessBuilder pb = new ProcessBuilder(command);
            pb.environment().put("PGPASSWORD", pass);
            Process process = pb.start();

            StringBuilder stdOut = new StringBuilder();
            StringBuilder stdErr = new StringBuilder();

            Thread outThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stdOut.append(line).append("\n");
                    }
                } catch (IOException e) {
                    stdErr.append("Error reading stdout: ").append(e.getMessage()).append("\n");
                }
            });

            Thread errThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stdErr.append(line).append("\n");
                    }
                } catch (IOException e) {
                    stdErr.append("Error reading stderr: ").append(e.getMessage()).append("\n");
                }
            });

            outThread.start();
            errThread.start();

            outThread.join();
            errThread.join();

            int exitCode = process.waitFor();

            output.append("STDOUT:\n").append(stdOut);
            output.append("STDERR:\n").append(stdErr);

            if (exitCode == 0) {
                return "Restore completed successfully.\n" + output;
            } else if (exitCode == 1) {
                return "(exit code 1).\n" + output;
            } else {
                return "Restore failed with exit code " + exitCode + ".\n" + output;
            }

        } catch (Exception e) {
            return "Exception occurred during restoreDatabase: " + e.getMessage();
        }
    }
    @Override
    public String getType() {
        return "POSTGRES";
    }
}
