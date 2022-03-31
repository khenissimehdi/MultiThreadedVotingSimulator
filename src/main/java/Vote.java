package main.java;

import java.util.HashMap;

public class Vote {
    private final HashMap<String, Integer> map = new HashMap<>();
    private final int nbVotes;
    private int currentVotes;
    private String winner;

    public Vote(int nbVotes) {
        this.nbVotes = nbVotes;
    }

    public String vote(String voter) throws InterruptedException {
        synchronized (map) {
            currentVotes++;
            map.merge(voter, 1, Integer::sum);
            while (nbVotes < currentVotes) {
                map.wait();
            }
            if(winner == null) {
                winner = computeWinner();
            }
            map.notifyAll();
            return winner;
        }

    }

    private String computeWinner() {
        int currentWinnerScore = -1;
        String currentWinner = null;
        for (var e : map.entrySet()) {
            if (e.getValue() > currentWinnerScore
                    || (e.getValue() == currentWinnerScore && e.getKey().compareTo(currentWinner) < 0)) {
                currentWinner = e.getKey();
                currentWinnerScore = e.getValue();
            }
        }
        return currentWinner;
    }

}
