import io.grpc.Server;
import io.grpc.ServerBuilder;
import network.utils.GrpcServer;
import org.example.Repository.Hibernate.AdminRepoHibernate;
import org.example.Repository.Hibernate.ClientRepoHibernate;
import org.example.Repository.RepositoryDB.AdminRepoDB;
import org.example.Repository.RepositoryDB.ClientRepoDB;
import org.example.Repository.RepositoryDB.ReservationRepoDB;
import org.example.Repository.RepositoryDB.TripRepoDB;
import server.ServicesImpl;
import services.IServices;

import java.io.IOException;
import java.util.Properties;

public class StartGrcpServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int defaultPort = 55556;

        Properties serverProps = new Properties();
        try{
            serverProps.load(StartJsonServer.class.getResourceAsStream("/server.properties"));
        }
        catch(Exception e){
            e.printStackTrace();
            return;
        }
//      ClientRepoDB repoClient = new ClientRepoDB(serverProps);
//      AdminRepoDB repoAdmin = new AdminRepoDB(serverProps);
        AdminRepoHibernate repoAdmin = new AdminRepoHibernate();
        ClientRepoHibernate repoClient = new ClientRepoHibernate();
        TripRepoDB repoTrip = new TripRepoDB(serverProps);
        ReservationRepoDB reservationRepoDB = new ReservationRepoDB(serverProps, repoClient, repoTrip);

        int serverPort = defaultPort;
        try{
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }

        System.out.println("Server port: " + serverPort);

        IServices ServerImpl = new ServicesImpl(repoAdmin, repoClient, repoTrip, reservationRepoDB);

        GrpcServer grcpService = new GrpcServer(ServerImpl);

        Server grpcServer = ServerBuilder.forPort(serverPort)
                .addService(grcpService)
                .build();

        System.out.println("Starting server on port " + serverPort);
        grpcServer.start();
        grpcServer.awaitTermination();
    }
}
