package common.commands.concreteCommands.serverOnly;

import client.reading.readers.Reader;
import common.commands.abstraction.Command;

import static server.launcher.CommandsLauncher.currentScripts;

/**
 * clear command
 */
public class Clear extends Command {

    @Override
    public Object execute() {
        collection.clear();
        return "collection cleared";
    }

    @Override
    public void setArgs(Reader from) {
        // do nothing
    }

    @Override
    public String toString() {
        String res = "clear";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
