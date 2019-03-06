package Domain;

import java.util.Date;

public class Game implements HasID<Integer> {
    private int ID;
    private String teamA;
    private String teamB;
    private int totalSeats;
    private String type;
    private Date date;

    public Game(int ID, String teamA, String teamB, int totalSeats, String type, Date date) {
        this.ID = ID;
        this.teamA = teamA;
        this.teamB = teamB;
        this.totalSeats = totalSeats;
        this.type = type;
        this.date = date;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer integer) {
        ID=integer;
    }
}
