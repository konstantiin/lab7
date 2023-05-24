package common.commands.abstraction;

import client.reading.readers.Reader;
import server.launcher.CommandsLauncher;

import java.io.Serializable;

/**
 * Abstract command class
 */
public abstract class Command implements Serializable {
    protected CommandsLauncher<?> collection;
    protected boolean send = true;

    /**
     * default constructor, initialize fields as null
     */
    public boolean ifSend() {
        return send;
    }

    public void setCollection(CommandsLauncher<?> collect) {
        collection = collect;
    }


    /**
     * executes command
     */
    public abstract Object execute();

    /**
     * sets arguments
     */
    public abstract void setArgs(Reader from);

    @Override
    public abstract String toString();
}
