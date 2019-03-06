package Domain;

public class Transaction implements HasID<Integer>{
    private int ID;
    private int ticketID;
    private String clientName;

    public Transaction(int ID, int ticketID, String clientName) {
        this.ID = ID;
        this.ticketID = ticketID;
        this.clientName = clientName;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer integer) {
        ID=integer;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
