package sample;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score>{
    private int points;
    private String nickname;
    private static final long serialVersionUID = -2507458165697461266L;

    public Score(int points, String nickname) {
        this.points = points;
        this.nickname = nickname;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return  "SCORE " + points +
                " NICKNAME = " + nickname;
    }


    @Override
    public int compareTo(Score o) {
        if(this.points > o.points)
            return 1;
        else if (this.points < o.points)
            return -1;
        return 0;
    }
}
