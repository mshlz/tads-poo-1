package com.mshlz.dao;

import java.sql.Connection;

import com.mshlz.interfaces.IBaseDAO;

public class BaseDAO<T> implements IBaseDAO<T> {
    protected Connection connection;

    public BaseDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Boolean save(T model) {
        return false;
    }

}
