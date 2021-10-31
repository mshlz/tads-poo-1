package com.mshlz.interfaces;

import com.mshlz.models.Card;

public interface IPlayableHand {
    public void addCard(Card card);
    public Integer getHandValue();
    public String getPreviewString();
}
