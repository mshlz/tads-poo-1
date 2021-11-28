package com.mshlz.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mshlz.models.Match;
import com.mshlz.models.User;
import com.mshlz.models.blackjack.DealerHand;
import com.mshlz.models.blackjack.PlayerHand;

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

    public List<Match> findLastMatches(User user, Integer size) {
        try {
            String sql = "" +
                    "SELECT" +
                    "    match.id as match_id," +
                    "    match.result as match_result," +
                    "    match.date as match_date," +
                    " " +
                    "    u.id as user_id," +
                    "    u.name as user_name," +
                    "    u.nickname as user_nickname," +
                    " " +
                    "    dh.id as dealer_hand_id," +
                    "    dh.busted as dealer_hand_busted," +
                    "    dh.blackjack as dealer_hand_blackjack," +
                    "    dh.value as dealer_hand_value," +
                    "    dh.description as dealer_hand_description," +
                    "    dh.date as dealer_hand_date," +
                    " " +
                    "    ph.id as player_hand_id," +
                    "    ph.busted as player_hand_busted," +
                    "    ph.blackjack as player_hand_blackjack," +
                    "    ph.value as player_hand_value," +
                    "    ph.description as player_hand_description," +
                    "    ph.date as player_hand_date" +
                    " " +
                    "FROM public.match match" +
                    "    INNER JOIN public.user u ON u.id = match.user_id" +
                    "    INNER JOIN public.dealer_hand dh ON dh.id = match.dealer_hand_id" +
                    "    INNER JOIN public.player_hand ph ON match.player_hand_id = ph.id" +
                    " " +
                    "WHERE u.id = ?\n" +
                    "ORDER BY match.date DESC\n" +
                    "LIMIT ?;";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, user.getId());
            statement.setInt(2, size);

            ResultSet queryResult = statement.executeQuery();
            List<Match> lastMatches = new ArrayList<>();

            while (queryResult.next()) {
                User _user = new User(
                        queryResult.getLong("user_id"),
                        queryResult.getString("user_name"),
                        queryResult.getString("user_nickname"));

                PlayerHand _playerHand = new PlayerHand(
                        queryResult.getLong("player_hand_id"),
                        queryResult.getBoolean("player_hand_busted"),
                        queryResult.getBoolean("player_hand_blackjack"),
                        queryResult.getInt("player_hand_value"),
                        queryResult.getString("player_hand_description"),
                        queryResult.getTimestamp("player_hand_date"),
                        _user);

                DealerHand _dealerHand = new DealerHand(
                        queryResult.getLong("dealer_hand_id"),
                        queryResult.getBoolean("dealer_hand_busted"),
                        queryResult.getBoolean("dealer_hand_blackjack"),
                        queryResult.getInt("dealer_hand_value"),
                        queryResult.getString("dealer_hand_description"),
                        queryResult.getTimestamp("dealer_hand_date"));

                Match _match = new Match(
                        queryResult.getLong("match_id"),
                        _user,
                        _dealerHand,
                        _playerHand,
                        queryResult.getString("match_result"),
                        queryResult.getTimestamp("match_date"));

                lastMatches.add(_match);
            }

            return lastMatches;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
