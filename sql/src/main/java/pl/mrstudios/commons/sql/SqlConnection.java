package pl.mrstudios.commons.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import pl.mrstudios.commons.sql.result.SqlEntry;
import pl.mrstudios.commons.sql.result.SqlResult;
import pl.mrstudios.commons.sql.statement.SqlStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collection;
import java.util.LinkedList;

import static java.lang.Class.forName;

public class SqlConnection {

    private final HikariConfig config;
    private HikariDataSource dataSource;

    public SqlConnection(
            @NotNull HikariConfig hikariConfig
    ) {
        this.config = hikariConfig;
        this.dataSource = new HikariDataSource(this.config);
    }

    @SuppressWarnings("all")
    public @NotNull Collection<SqlResult> fetch(
            @NotNull SqlStatement statement
    ) {

        Collection<SqlResult> result = new LinkedList<>();

        if (this.dataSource.isClosed())
            this.dataSource = new HikariDataSource(this.config);

        try (
                Connection connection = this.dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement.query())
        ) {

            statement.prepare(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next())
                    result.add(handleReceivedResultSet(resultSet));
            }

        } catch (@NotNull Exception exception) {
            throw new RuntimeException("Unable to fetch entries from database due to exception.", exception);
        }

        return result;

    }

    @SuppressWarnings("all")
    public void execute(
            @NotNull SqlStatement statement
    ) {

        try (
                Connection connection = this.dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement.query())
        ) {
            statement.prepare(preparedStatement);
            preparedStatement.execute();
        } catch (@NotNull Exception exception) {
            throw new RuntimeException("Unable to execute statement due to exception.", exception);
        }

    }

    protected static @NotNull SqlResult handleReceivedResultSet(
            @NotNull ResultSet resultSet
    ) {

        try {

            SqlResult result = new SqlResult();
            ResultSetMetaData metaData = resultSet.getMetaData();

            for (int i = 1; i <= metaData.getColumnCount(); i++)
                result.add(new SqlEntry(metaData.getColumnName(i), forName(metaData.getColumnClassName(i)), resultSet.getObject(i)));

            return result;

        } catch (@NotNull Exception exception) {
            throw new RuntimeException("Unable to handle received result due to exception.", exception);
        }

    }

}
