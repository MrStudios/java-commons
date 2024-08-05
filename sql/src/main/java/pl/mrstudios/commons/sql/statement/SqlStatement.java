package pl.mrstudios.commons.sql.statement;

import org.jetbrains.annotations.NotNull;
import pl.mrstudios.commons.sql.SqlConnection;
import pl.mrstudios.commons.sql.result.SqlResult;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.sql.JDBCType.*;

public class SqlStatement {

    private final String query;
    private final Map<Integer, SqlStatementObject> elements;

    protected SqlStatement(
            @NotNull String query
    ) {
        this.query = query;
        this.elements = new HashMap<>();
    }

    public @NotNull SqlStatement setString(
            @NotNull Integer position,
            @NotNull String value
    ) {
        return this.set(position, VARCHAR, value);
    }

    public @NotNull SqlStatement setLongString(
            @NotNull Integer position,
            @NotNull String value
    ) {
        return this.set(position, LONGVARCHAR, value);
    }

    public @NotNull SqlStatement setInteger(
            @NotNull Integer position,
            @NotNull Integer value
    ) {
        return this.set(position, INTEGER, value);
    }

    public @NotNull SqlStatement setDouble(
            @NotNull Integer position,
            @NotNull Double value
    ) {
        return this.set(position, DOUBLE, value);
    }

    public @NotNull SqlStatement setLong(
            @NotNull Integer position,
            @NotNull Long value
    ) {
        return this.set(position, BIGINT, value);
    }

    public @NotNull SqlStatement setFloat(
            @NotNull Integer position,
            @NotNull Float value
    ) {
        return this.set(position, FLOAT, value);
    }

    public @NotNull SqlStatement set(
            @NotNull Integer position,
            @NotNull JDBCType type,
            @NotNull Object value
    ) {
        this.elements.put(position, new SqlStatementObject(position, type, value));
        return this;
    }

    public @NotNull String query() {
        return this.query;
    }

    public void prepare(
            @NotNull PreparedStatement preparedStatement
    ) {

        this.elements.forEach((position, object) -> {
            try {
                preparedStatement.setObject(position, object.object(), object.type());
            } catch (@NotNull Exception exception) {
                throw new RuntimeException("Unable to prepare statement due to exception.", exception);
            }
        });

    }

    public void execute(
            @NotNull SqlConnection sqlConnection
    ) {
        sqlConnection.execute(this);
    }

    public @NotNull Collection<SqlResult> fetch(
            @NotNull SqlConnection sqlConnection
    ) {
        return sqlConnection.fetch(this);
    }

    public static @NotNull SqlStatement createStatement(
            @NotNull String query
    ) {
        return new SqlStatement(query);
    }

}
