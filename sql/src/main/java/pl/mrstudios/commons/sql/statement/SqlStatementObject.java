package pl.mrstudios.commons.sql.statement;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLType;

record SqlStatementObject(
        @NotNull Integer position,
        @NotNull SQLType type,
        @NotNull Object object
) {}
