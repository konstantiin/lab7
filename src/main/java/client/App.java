package client;


import client.connection.ConnectToServer;
import client.reading.objectTree.Node;
import client.reading.readers.OnlineReader;
import common.commands.abstraction.Command;
import common.exceptions.inputExceptions.InputException;
import common.exceptions.inputExceptions.UnknownCommandException;
import common.storedClasses.forms.HumanBeingForm;


public class App {
    public static ConnectToServer server;
    public static OnlineReader console;

    /**
     * main method
     * creates managed collection, parses xml file and execute commands from System.in
     */
    public static void main(String[] args) {

        int port = Integer.parseInt(args[1]);
        server = ConnectToServer.getServer(args[0], port);
        if (server == null) {
            System.out.println("Connection failed.");
            return;
        }

        console = new OnlineReader(System.in, Node.generateTree(HumanBeingForm.class, "HumanBeing"));
        while (console.hasNext()) {
            Command met = null;
            try {
                met = console.readCommand();
            } catch (UnknownCommandException e) {
                System.out.println("Command not found, type \"help\" for more info");
            } catch (InputException e) {
                console.renewScan(System.in);
            }
            if (met != null) {
                if (met.ifSend()) {
                    try {
                        server.sendCommand(met);
                        System.out.println(server.getResponse());
                    } catch (Exception e) {
                        System.out.println("Execution ended");
                        console.closeStream();
                    }

                } else {
                    met.execute();
                }
            }
        }

    }
}