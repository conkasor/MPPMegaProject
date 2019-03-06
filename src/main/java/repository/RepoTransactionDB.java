package repository;

import Domain.Game;
import Domain.Ticket;
import Domain.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class RepoTransactionDB extends AbstractRepo<Integer, Transaction> {
    private JdbcUtils dbUtils;
    private IRepository<Integer,Ticket> ticketRepo;

    public RepoTransactionDB(JdbcUtils dbUtils, IRepository<Integer, Ticket> ticketRepo) {
        this.dbUtils = dbUtils;
        this.ticketRepo = ticketRepo;
        logger.info("Initializing SortingTaskRepository with properties: {} ", dbUtils);

    }

    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger logger = LogManager.getLogger();



    @Override
    public int size() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from Transactions")) {
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
    public void save(Transaction entity) throws RepositoryException {
        logger.traceEntry("saving transaction {} ", entity);
        Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement("insert into Transactions values (?,?,?)")) {
                if (ticketRepo.findOne(entity.getTicketID()) == null)
                    throw new RepositoryException("The game that you want to add tickets to " +
                            "doesn't exist");
                preStmt.setInt(1, entity.getID());
                preStmt.setInt(2, entity.getTicketID());
                preStmt.setString(3, entity.getClientName());
                int result = preStmt.executeUpdate();
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
            logger.traceExit();
        }

    @Override
    public void update(Integer integer, Transaction entity) {
        logger.traceEntry("Update transaction {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Transactions set ID=?,ticketID=?,clientName=? where ID="+integer)) {
            if (ticketRepo.findOne(entity.getTicketID())==null) throw new RepositoryException("The game that you want to add tickets to " +
                    "doesn't exist");
            preStmt.setInt(1, entity.getID());
            preStmt.setInt(2, entity.getTicketID());
            preStmt.setString(3, entity.getClientName());
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
        logger.traceEntry("deleting transaction with {}", integer);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE from Transactions where ID=?")) {
            preStmt.setInt(1, integer);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();

    }

    @Override
    public Transaction findOne(Integer integer) {

        logger.traceEntry("finding ticket with id {} ", integer);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from Transactions where ID=?")) {
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("ID");
                    String clientName = result.getString("clientName");
                    int tID = result.getInt("ticketID");
                    Transaction transaction = new Transaction(id,tID,clientName);
                    logger.traceExit(transaction);
                    return transaction;
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
    public Iterable<Transaction> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Transaction> transactions=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Transactions")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("ID");
                    String clientName = result.getString("clientName");
                    int tID = result.getInt("ticketID");
                    Transaction transaction = new Transaction(id,tID,clientName);
                    transactions.add(transaction);
                }
            } catch (SQLException e) {
                logger.error(e);
                System.out.println("Error DB " + e);
            }
            logger.traceExit(transactions);
            return transactions;

        } catch (SQLException e) {

            return null;
        }
    }
}
