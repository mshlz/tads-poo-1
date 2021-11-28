package com.mshlz.models.blackjack;

import java.util.Date;

import com.mshlz.models.User;

public class BaseHand {
    protected Long id;
    protected Long user_id;
    protected Boolean busted;
    protected Boolean blackjack;
    protected Integer value;
    protected String description;
    protected Date date;

    BaseHand(User user) {
        if (user != null) {
            this.user_id = user.getId();
        }
        this.blackjack = false;
        this.busted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.user_id;
    }

    public void setUserId(Long id) {
        this.user_id = id;
    }

    public Boolean getBlackjack() {
        return blackjack;
    }

    public void setBlackjack(Boolean balckjack) {
        this.blackjack = balckjack;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBusted(Boolean busted) {
        this.busted = busted;
    }
}
