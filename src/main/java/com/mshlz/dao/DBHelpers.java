package com.mshlz.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelpers {
    static Connection connection = DatabaseConnection.getConnection();

    public static void migrateDB() {
        try {
            Statement statement;
            statement = connection.createStatement();
            // user table
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS public.user (id bigserial primary key, name varchar(255), nickname varchar(255));");
            // dealer hand table
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS public.dealer_hand (id bigserial primary key, busted bool, blackjack bool, value integer, description varchar(255), date timestamp default now());");
            // player hand table
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS public.player_hand (id bigserial primary key, busted bool, blackjack bool, value integer, description varchar(255), user_id bigint, date timestamp default now());");
            // match table
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS public.match (id bigserial primary key, user_id bigint, dealer_hand_id bigint, player_hand_id bigint, result varchar(255), date timestamp default now());");

            statement.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("[DB HELPER]: Failed to run migration");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
        }
    }

    public static void dropDB() {
        try {
            System.out.println("Dropping all database tables...");
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            // user table
            statement.executeUpdate("DROP TABLE IF EXISTS public.user");
            // dealer hand table
            statement.executeUpdate("DROP TABLE IF EXISTS public.dealer_hand");
            // player hand table
            statement.executeUpdate("DROP TABLE IF EXISTS public.player_hand");
            // match table
            statement.executeUpdate("DROP TABLE IF EXISTS public.match");

            statement.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("[DB HELPER]: Failed to run the drop tables");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
        }
    }

}
