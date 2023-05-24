package server.main;


import common.commands.abstraction.Command;
import common.connection.ObjectByteArrays;
import common.storedClasses.HumanBeing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.connection.ConnectToClient;
import server.launcher.CommandsLauncher;
import server.parse.ParseXml;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {
    public static final ParseXml XMLInput = ParseXml.getXMLInput("input.xml");
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        int port = Integer.parseInt(args[0]);
        var server = new ConnectToClient(port);
        System.out.println("Server is running. Port " + port);
        logger.info("Server is running. Port " + port);
        CommandsLauncher<HumanBeing> collection = new CommandsLauncher<>(new TreeSet<>(XMLInput.getArr()));
        boolean work = true;
        Scanner console = new Scanner(System.in);
        while (work) {
            try {
                ConnectToClient.readAll(server);
                var input = server.getInputObjects();
                input.forEach((key, value) -> {
                    Command c = (Command) value.toObject();
                    c.setCollection(collection);
                    server.setAnswer(key, ObjectByteArrays.getArrays((Serializable) c.execute()));
                });
                ConnectToClient.writeAll(server);

                int b = System.in.available();
                if (b > 0) {
                    logger.info("Server command was found.");
                    work = collection.runServerCommand(console.next().trim().toLowerCase());
                    logger.info("Command compiled");
                }

            } catch (IOException e) {
                logger.error("Unknown error" + e.getMessage());
                throw new RuntimeException(e);

            }

        }
        console.close();
        server.close();
    }
}
