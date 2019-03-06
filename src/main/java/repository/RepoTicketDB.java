package repository;

import Domain.Game;
import Domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
public class RepoTicketDB extends AbstractRepo<Integer, Ticket> {
    private JdbcUtils dbUtils;
    private IRepository<Integer,Game> gameRepo;

    public RepoTicketDB(JdbcUtils dbUtils, IRepository<Integer, Game> gameRepo) {
        this.dbUtils = dbUtils;
        this.gameRepo = gameRepo;
        logger.info("Initializing SortingTaskRepository with properties: {} ", dbUtils);

    }

   // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger logger = LogManager.getLogger();



    @Override
    public int size() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from Tickets")) {
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
        return 0;

    }

    @Override
    public void save(Ticket entity) throws RepositoryException{
        logger.traceEntry("saving Ticket {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Tickets values (?,?,?,?)")) {
            if (gameRepo.findOne(entity.getGameID())==null) throw new RepositoryException("The game that you want to add tickets to " +
                    "doesn't exist");
            preStmt.setInt(1, entity.getID());
            preStmt.setInt(2, entity.getGameID());
            preStmt.setString(3, entity.getIdentification());
            preStmt.setInt(4, entity.getPrice());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        catch (RepositoryException e)
        {
            e.printStackTrace();
        }
        logger.traceExit();

    }

    @Override
    public void update(Integer integer, Ticket entity) {
        logger.traceEntry("Update Ticket {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Tickets set ID=?,gameId=?,identification=?,price=? where ID="+integer)) {
            if (gameRepo.findOne(entity.getGameID())==null) throw new RepositoryException("The game that you want to add tickets to " +
                    "doesn't exist");
            preStmt.setInt(1, entity.getID());
            preStmt.setInt(2, entity.getGameID());
            preStmt.setString(3, entity.getIdentification());
            preStmt.setInt(4, entity.getPrice());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        catch (RepositoryException e)
        {
            e.printStackTrace();
        }
        logger.traceExit();

    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting ticket with {}", integer);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Tickets where ID=?")) {
            preStmt.setInt(1, integer);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();

    }

    @Override
    public Ticket findOne(Integer integer) {

        logger.traceEntry("finding ticket with id {} ", integer);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from Tickets where ID=?")) {
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("ID");
                    String identification = result.getString("identification");
                    int price = result.getInt("price");
                    int gameID = result.getInt("gameID");
                    Ticket ticket = new Ticket(id,gameID,identification,price);
                    logger.traceExit(ticket);
                    return ticket;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No ticket found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Ticket> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Ticket> tickets=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Tickets")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("ID");
                    String identification = result.getString("identification");
                    int price = result.getInt("price");
                    int gameID = result.getInt("gameID");
                    Ticket ticket = new Ticket(id, gameID, identification, price);
                    tickets.add(ticket);
                }
            } catch (SQLException e) {
                logger.error(e);
                System.out.println("Error DB " + e);
            }
            logger.traceExit(tickets);
            return tickets;

        } catch (SQLException e) {

            return null;
        }
    }
}
