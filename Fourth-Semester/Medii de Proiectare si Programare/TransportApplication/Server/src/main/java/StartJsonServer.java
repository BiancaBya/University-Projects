import network.jsonprotocol.ClientJsonWorker;
import network.utils.AbstractServer;
import network.utils.JsonConcurrentServer;
import network.utils.ServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Repository.AdminRepoInterface;
import org.example.Repository.ClientRepoInterface;
import org.example.Repository.Hibernate.AdminRepoHibernate;
import org.example.Repository.Hibernate.ClientRepoHibernate;
import org.example.Repository.RepositoryDB.AdminRepoDB;
import org.example.Repository.RepositoryDB.ClientRepoDB;
import org.example.Repository.RepositoryDB.ReservationRepoDB;
import org.example.Repository.RepositoryDB.TripRepoDB;
import org.example.Repository.ReservationRepoInterface;
import org.example.Repository.TripRepoInterface;
import server.ServicesImpl;
import services.IServices;

import java.io.IOException;
import java.util.Properties;

public class StartJsonServer {

    private static int defaultPort=55555;
    private static Logger logger = LogManager.getLogger(StartJsonServer.class);

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try{
            serverProps.load(StartJsonServer.class.getResourceAsStream("/server.properties"));
            logger.info("Server properties loaded {}", serverProps);
        } catch (IOException e) {
            logger.error("Cannot find server properties{}", e);
            return;
        }

//        AdminRepoDB repoAdmin = new AdminRepoDB(serverProps);
//        ClientRepoDB repoClient = new ClientRepoDB(serverProps);
        AdminRepoHibernate repoAdmin = new AdminRepoHibernate();
        ClientRepoHibernate repoClient = new ClientRepoHibernate();
        TripRepoDB repoTrip = new TripRepoDB(serverProps);
        ReservationRepoDB reservationRepoDB = new ReservationRepoDB(serverProps, repoClient, repoTrip);
        IServices ServerImpl = new ServicesImpl(repoAdmin, repoClient, repoTrip, reservationRepoDB);

        int ServerPort=defaultPort;
        try {
            ServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            logger.error("Wrong  Port Number{}", nef.getMessage());
            logger.debug("Using default port {}", defaultPort);
        }
        logger.debug("Starting server on port: {}", ServerPort);
        AbstractServer server = new JsonConcurrentServer(ServerPort, ServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            logger.error("Error starting the server{}", e.getMessage());
        }
    }
}



