package pl.mrstudios.commons.sql.statement;

import org.jetbrains.annotations.NotNull;

import java.sql.JDBCType;

record SqlStatementObject(
        @NotNull Integer position,
        @NotNull JDBCType type,
        @NotNull Object object
) {}
