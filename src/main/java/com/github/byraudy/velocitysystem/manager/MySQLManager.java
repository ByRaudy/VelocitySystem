package com.github.byraudy.velocitysystem.manager;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.github.byraudy.velocitysystem.manager.config.MySQL;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class MySQLManager {

    private final String host;
    private final String user;
    private final String password;
    private final String database;
    private final int port;
    private final ExecutorService executorService;
    private Connection connection;

    public MySQLManager(MySQL mySQL) throws SQLException {
        this.host = mySQL.getString("mysql_host");
        this.port = mySQL.getInt("mysql_port");
        this.user = mySQL.getString("mysql_user");
        this.password = mySQL.getString("mysql_password");
        this.database = mySQL.getString("mysql_database");
        this.executorService = Executors.newCachedThreadPool();

        connectToDatabase();
    }

    public void connectToDatabase() throws SQLException {
        if (!isConnectedToDatabase()) {
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setServerName(host);
            mysqlDataSource.setPort(port);
            mysqlDataSource.setUser(user);
            mysqlDataSource.setPassword(password);
            mysqlDataSource.setDatabaseName(database);
            connection = mysqlDataSource.getConnection();

            updateDatabase("CREATE TABLE IF NOT EXISTS bannedPlayerData (playerName VARCHAR(100), playerUuid VARCHAR(100), reason VARCHAR(100), time LONG, bannersName VARCHAR(100))");
            updateDatabase("CREATE TABLE IF NOT EXISTS mutedPlayerData (playerName VARCHAR(100), playerUuid VARCHAR(100), reason VARCHAR(100), time LONG, bannersName VARCHAR(100))");
        }
    }

    public boolean isConnectedToDatabase() {
        return connection != null;
    }

    public void disconnectFromDatabase() {
        if (isConnectedToDatabase()) {
            try {
                connection.close();
                System.out.println("[VelocitySystem] MySQL Connection »» Closed");
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    public Connection getConnectionToDatabase() {
        return connection;
    }

    public void updateDatabase(String query) {
        executorService.execute(() -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
            } catch (SQLException var11) {
                var11.printStackTrace();
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException var10) {
                    var10.printStackTrace();
                }
            }
        });
    }

    public ResultSet getResultSetWithStatement(PreparedStatement statement) {
        if (connection != null) {
            Future<ResultSet> future = executorService.submit(() -> {
                try {
                    return statement.executeQuery();
                } catch (SQLException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            });
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public void updateDatabaseWithStatement(PreparedStatement preparedStatement) {
        if (connection != null) {
            VelocitySystem.getVelocitySystem().getProxyServer().getScheduler().buildTask(VelocitySystem.getVelocitySystem(), () -> {
                try {
                    preparedStatement.execute();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    public ResultSet getDatabaseResult(String query) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }
}
