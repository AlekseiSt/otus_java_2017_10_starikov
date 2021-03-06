package ru.otus.hw9.sql.executor;

import ru.otus.hw9.data.DataSet;

import java.sql.*;

import static ru.otus.hw9.data.reflection.DataSetWorker.createDataSet;

/**
 * Class for execute sql statements
 */
public class SqlExecutor {
    private Connection connection;

    public SqlExecutor(Connection connection) {
        this.connection = connection;
    }

    public <T extends DataSet> T select(String query, long id, Class<T> clazz) throws Exception {
        try(Statement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery(query);
            T dataSet = createDataSet(clazz, resultSet);
            if (dataSet != null) {
                dataSet.setId(id);
            }
            return dataSet;
        }
    }

    public long insert(String query) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getLong(1);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        connection.close();
    }
}
