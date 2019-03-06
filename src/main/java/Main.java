import Domain.Ticket;
import Domain.Transaction;
import repository.JdbcUtils;
import repository.RepoGameDB;
import repository.RepoTicketDB;
import repository.RepoTransactionDB;

import java.io.*;
import java.util.Date;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties prop = new Properties();

        InputStream input = null;
        try {
            input = new FileInputStream("jdbcprops.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert input != null;
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RepoGameDB r=new RepoGameDB(new JdbcUtils(prop));
        RepoTicketDB rt=new RepoTicketDB(new JdbcUtils(prop),r);
        RepoTransactionDB rtr=new RepoTransactionDB(new JdbcUtils(prop),rt);
        //System.out.println(rt.findAll());
        rtr.save(new Transaction(2,1,"George"));
        System.out.println(rtr.findAll());

        //rt.save(new Ticket(4,1,"22b",222));
        Date d=new Date();
       // r.update(2,new Game(2,"22","2",30,"44",d));

        //System.out.println(r.findOne(2).getTeamA());
        //System.out.println(r.findAll());
        //9
        // r1   a,
        // ].delete(1);
        //System.out.println(r.size());

    }
}
