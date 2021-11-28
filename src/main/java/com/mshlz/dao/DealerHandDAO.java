package com.mshlz.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mshlz.models.blackjack.DealerHand;

public class DealerHandDAO extends BaseDAO<DealerHand> {

    @Override
    public Boolean save(DealerHand hand) {
        try {
            String sql = "INSERT INTO public.dealer_hand (busted, blackjack, value, description) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setBoolean(1, hand.isBusted());
            statement.setBoolean(2, hand.getBlackjack());
            statement.setInt(3, hand.getHandValue());
            statement.setString(4, hand.getPreviewString(true));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creation failed, no rows affected.");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (!generatedKeys.next()) {
                throw new SQLException("Creation failed, no id generated.");
            }

            hand.setId(generatedKeys.getLong(1));

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

}