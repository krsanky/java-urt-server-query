package org.oldcode.urt;

public class Player {
    private String name;
    private int ping;
    private int score;

    public Player(String name, int score, int ping) {
        this.name = name;
        this.ping= ping;
        this.score = score;
    }

    public Player(String name, String score, String ping) {
        this(name, Integer.parseInt(score), Integer.parseInt(ping));
    }

    @Override
    public String toString() {
        return "<Player "+this.name+" score:"+this.score+" ping:"+this.ping+">"; 
    }

    //this strips colors (strip spaces too?)
    public void cleanName() {
        this.name = Util.stripColors(this.name);
    }

}

