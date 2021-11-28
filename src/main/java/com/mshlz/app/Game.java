package com.mshlz.app;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.mshlz.dao.DealerHandDAO;
import com.mshlz.dao.MatchDAO;
import com.mshlz.dao.PlayerHandDAO;
import com.mshlz.exceptions.EmptyDeckException;
import com.mshlz.models.Deck;
import com.mshlz.models.FrenchDeck;
import com.mshlz.models.Match;
import com.mshlz.models.User;
import com.mshlz.models.blackjack.DealerHand;
import com.mshlz.models.blackjack.PlayerHand;

public class Game {
    public static final String TITLE = "Simple Blackjack";
    final String PLAYER_WIN = "PLAYER_WIN", DEALER_WIN = "DEALER_WIN", DRAW = "DRAW";
    User user;
    boolean renderMenu = true;

    public Game(User user) {
        this.user = user;
    }

    public void start() {
        showMainMenu();
    }

    // ---- MAIN GAME -----------------------------------------------------
    // --------------------------------------------------------------------
    private void showMainMenu() {
        String[] availableOptions = { "Nova partida", "Ver partidas passadas", "Sair" };

        while (this.renderMenu) {
            int result = JOptionPane.showOptionDialog(null, "O que você deseja fazer?", TITLE,
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, availableOptions, null);

            switch (result) {

                case JOptionPane.YES_OPTION:
                    initGame();
                    break;

                case JOptionPane.NO_OPTION:
                    this.renderMenu = false;
                    JOptionPane.showMessageDialog(null, "Suas partidas: xxx", TITLE, JOptionPane.INFORMATION_MESSAGE);
                    showLastMatches();
                    break;

                case JOptionPane.CANCEL_OPTION:
                default:
                    JOptionPane.showMessageDialog(null, "Até mais.", TITLE, JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
            }
        }
    }

    private void initGame() {
        PlayerHandDAO playerDao = new PlayerHandDAO();
        DealerHandDAO dealerDao = new DealerHandDAO();
        MatchDAO matchDao = new MatchDAO();

        String[] actions = { "Hit (+1)", "Stand" };

        try {
            int rounds = 0;
            int action;
            String matchResult;

            Deck deck = new FrenchDeck();
            deck.shuffle();

            while (!deck.isEmpty()) {
                ++rounds;
                action = -1;
                matchResult = null;

                DealerHand dealerHand = new DealerHand();
                PlayerHand playerHand = new PlayerHand(this.user);

                for (int i = 0; i < 2; ++i) {
                    playerHand.addCard(deck.drawCard());
                    dealerHand.addCard(deck.drawCard());
                }

                if (playerHand.getHandValue() == 21) {
                    playerHand.setBlackjack(true);
                    matchResult = PLAYER_WIN;
                    this.showMessage(
                            TITLE + " - Jogada #" + rounds + " - BLACKJACK!",
                            "Você fez Blackjack!\n\n" + getRoundInfo(dealerHand, playerHand, true),
                            false);
                }

                // only ask if has no blackjack
                if (matchResult == null) {
                    do {
                        action = JOptionPane.showOptionDialog(null,
                                getRoundInfo(dealerHand, playerHand, false) + "\n" + "Qual sua próxima ação?",
                                "Jogada #" + (rounds), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                                actions, null);

                        switch (action) {
                            case JOptionPane.YES_OPTION:
                                playerHand.addCard(deck.drawCard());
                                System.out.println("[Player]: Hit");
                                break;
                            case JOptionPane.NO_OPTION:
                                System.out.println("[Player]: Stand\n");
                                break;
                            default:
                                action = Integer.MIN_VALUE;
                                continue;
                        }
                    } while (!playerHand.isBusted() && action == JOptionPane.YES_OPTION);

                    if (playerHand.isBusted()) {
                        matchResult = DEALER_WIN;
                    }

                    if (action == Integer.MIN_VALUE) {
                        this.showMessage(TITLE, "Você abandonou a partida!", false);
                        return;
                    }
                }

                if (dealerHand.getHandValue() == 21) {
                    dealerHand.setBlackjack(true);
                } else if (matchResult != DEALER_WIN) {
                    while (dealerHand.getHandValue() < 16) {
                        dealerHand.addCard(deck.drawCard());
                    }
                }

                int playerSum = playerHand.getHandValue();
                int dealerSum = dealerHand.getHandValue();

                if (matchResult == null) {
                    if (playerHand.isBusted() || (!dealerHand.isBusted() && playerSum < dealerSum)) {
                        matchResult = DEALER_WIN;
                    } else if (dealerHand.isBusted() || playerSum > dealerSum) {
                        matchResult = PLAYER_WIN;
                    } else {
                        matchResult = DRAW;
                    }
                }

                switch (matchResult) {
                    case PLAYER_WIN:
                        this.showMessage(TITLE, "Você ganhou!\n" + getRoundInfo(dealerHand, playerHand, true), false);
                        break;
                    case DEALER_WIN:
                        this.showMessage(TITLE, "Você perdeu!\n" + getRoundInfo(dealerHand, playerHand, true), true);
                        break;
                    case DRAW:
                        this.showMessage(TITLE, "Empate!\n" + getRoundInfo(dealerHand, playerHand, true), false);
                }

                // save the match
                playerDao.save(playerHand);
                dealerDao.save(dealerHand);

                Match match = new Match();
                match.setUser(user);
                match.setPlayerHand(playerHand);
                match.setDealerHand(dealerHand);
                match.setResult(matchResult);

                matchDao.save(match);
            }
        } catch (EmptyDeckException exception) {
            this.showMessage("Erro - " + TITLE, "Ops! Acabaram as cartas do baralho!", true);
        }
    }

    // ---- SHOW LAST MATCHES -------------------------------------
    // ------------------------------------------------------------
    private void showLastMatches() {
        MatchDAO matchDao = new MatchDAO();
        List<Match> lastMatches = matchDao.findLastMatches(this.user, 50);

        JFrame frame = new JFrame(TITLE.concat(" - Suas últimas partidas"));
        SimpleDateFormat dtFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        String[][] data = lastMatches.stream().map(match -> {
            String[] line = {
                    dtFormatter.format(match.getDate()),
                    match.getResult(),
                    match.getPlayerHand().getDescription(),
                    match.getDealerHand().getDescription()
            };
            return line;
        }).toArray(String[][]::new);

        String[] header = { "Data", "Resultado", "Sua mão", "Mão do Dealer" };
        JTable table = new JTable(data, header);

        frame.add(new JScrollPane(table));
        frame.setSize(800, 600);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    // ---- HELPERS ---------------------------------------------------------
    // ----------------------------------------------------------------------
    private static String getRoundInfo(DealerHand dealer, PlayerHand player, Boolean revealDealerCards) {
        return dealer.getPreviewString(revealDealerCards) + "\n" + player.getPreviewString() + "\n";
    }

    private void showMessage(String title, String message, Boolean isError) {
        JOptionPane.showMessageDialog(null, message, title,
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}
