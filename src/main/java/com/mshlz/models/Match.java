package com.mshlz.models;

import java.util.Date;

import com.mshlz.models.blackjack.DealerHand;
import com.mshlz.models.blackjack.PlayerHand;

public class Match {
    private Long id;
    private User user;
    private DealerHand dealerHand;
    private PlayerHand playerHand;
    private String result;
    private Date date;

    public Match() {
    }

    public Match(Long id, User user, DealerHand dealerHand, PlayerHand playerHand, String result, Date date) {
        this.id = id;
        this.user = user;
        this.dealerHand = dealerHand;
        this.playerHand = playerHand;
        this.result = result;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DealerHand getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(DealerHand dealerHand) {
        this.dealerHand = dealerHand;
    }

    public PlayerHand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(PlayerHand playerHand) {
        this.playerHand = playerHand;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Match [date=" + date + ", dealerHand=" + dealerHand + ", id=" + id + ", playerHand=" + playerHand
                + ", result=" + result + ", user=" + user + "]";
    }
}
