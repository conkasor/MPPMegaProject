package Domain;

public class Ticket implements HasID<Integer> {
    private int ID;
    private int gameID;
    private String identification;
    private int price;

    public Ticket(int ID, int gameID, String identification, int price) {
        this.ID = ID;
        this.gameID = gameID;
        this.identification = identification;
        this.price = price;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer integer) {
        ID=integer;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
