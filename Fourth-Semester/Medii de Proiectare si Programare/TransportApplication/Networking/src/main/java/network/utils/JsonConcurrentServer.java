package network.utils;

import network.jsonprotocol.ClientJsonWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IServices;

import java.net.Socket;

public class JsonConcurrentServer extends AbsConcurrentServer{

    private IServices server;
    private static Logger logger = LogManager.getLogger(JsonConcurrentServer.class);

    public JsonConcurrentServer(int port, IServices server){
        super(port);
        this.server=server;
        logger.info("JsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientJsonWorker worker = new ClientJsonWorker(server, client);
        Thread thread=new Thread(worker);
        return thread;
    }
}



