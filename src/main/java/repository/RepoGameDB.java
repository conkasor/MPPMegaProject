package repository;

import Domain.Game;
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
public class RepoGameDB extends AbstractRepo<Integer, Game> {
    private JdbcUtils dbUtils;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger logger = LogManager.getLogger();


    public RepoGameDB(JdbcUtils dbUtils) {
        logger.info("Initializing SortingTaskRepository with properties: {} ", dbUtils);
        this.dbUtils = dbUtils;
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from Games")) {
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
        return 0;
    }

    @Override
    public void save(Game entity) {
        logger.traceEntry("saving Game {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Games values (?,?,?,?,?,?)")) {
            preStmt.setInt(1, entity.getID());
            preStmt.setString(2, entity.getTeamA());
            preStmt.setString(3, entity.getTeamB());
            preStmt.setInt(4, entity.getTotalSeats());
            preStmt.setString(5, entity.getType());
            preStmt.setString(6, format.format(entity.getDate()));
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();

    }

    @Override
    public void update(Integer integer, Game entity) {
        logger.traceEntry("Update Game {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Games set ID=?,teamA=?,teamB=?,totalSeats=?,type=?,gameDate=? where ID="+integer)) {
            preStmt.setInt(1, entity.getID());
            preStmt.setString(2, entity.getTeamA());
            preStmt.setString(3, entity.getTeamB());
            preStmt.setInt(4, entity.getTotalSeats());
            preStmt.setString(5, entity.getType());
            preStmt.setString(6, format.format(entity.getDate()));
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();

    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting task with {}", integer);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Games where id=?")) {
            preStmt.setInt(1, integer);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();

    }

    @Override
    public Game findOne(Integer integer) {

        logger.traceEntry("finding task with id {} ", integer);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from Games where ID=?")) {
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String teamA = result.getString("teamA");
                    String teamB = result.getString("teamB");
                    String type = result.getString("type");
                    int seats = result.getInt("totalSeats");
                    Date data = format.parse(result.getString("gameDate"));
                    Game game = new Game(id, teamA, teamB, seats, type, data);
                    logger.traceExit(game);
                    return game;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No task found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Game> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Game> games=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Games")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String teamA = result.getString("teamA");
                    String teamB = result.getString("teamB");
                    String type = result.getString("type");
                    int seats = result.getInt("totalSeats");
                    Date data = format.parse(result.getString("gameDate"));
                    Game game = new Game(id, teamA, teamB, seats, type, data);
                      games.add(game);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(games);
        return games;

    }
}
