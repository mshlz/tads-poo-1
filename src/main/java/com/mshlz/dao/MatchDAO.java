package com.mshlz.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mshlz.models.Match;
import com.mshlz.models.User;

public class MatchDAO extends BaseDAO<Match> {

    @Override
    public Boolean save(Match match) {
        try {
            String sql = "INSERT INTO public.match (user_id, dealer_hand_id, player_hand_id, result) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, match.getUser().getId());
            statement.setLong(2, match.getDealerHand().getId());
            statement.setLong(3, match.getPlayerHand().getId());
            statement.setString(4, match.getResult());
            
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
