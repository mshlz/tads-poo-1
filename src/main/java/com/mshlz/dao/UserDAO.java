package com.mshlz.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mshlz.models.User;

public class UserDAO extends BaseDAO<User> {

    @Override
    public Boolean save(User user) {
        try {
            String sql = "INSERT INTO public.user (name, nickname) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user.getName());
            statement.setString(2, user.getNickname());
            statement.execute();

            connection.commit();

            return true;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return false;
    }

    public User findOneByNickname(String nickname) {
        try {
            String sql = "SELECT id, name, nickname FROM public.user WHERE nickname = ? LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, nickname);

            ResultSet queryResult = statement.executeQuery();

            if (queryResult.next()) {
                return new User(
                        queryResult.getLong("id"),
                        queryResult.getString("name"),
                        queryResult.getString("nickname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
