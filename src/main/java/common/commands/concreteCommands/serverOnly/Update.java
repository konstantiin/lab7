package common.commands.concreteCommands.serverOnly;

import client.reading.readers.Reader;
import common.commands.abstraction.Command;
import common.exceptions.inputExceptions.IdException;
import common.storedClasses.HumanBeing;
import common.storedClasses.forms.HumanBeingForm;

import java.math.BigInteger;

import static server.launcher.CommandsLauncher.currentScripts;

/**
 * update command
 */
public class Update extends Command {
    private long id;
    private Object arg;

    @Override
    public Object execute() {
        try {
            collection.update(id, new HumanBeing((HumanBeingForm) arg));
            return "Element updated";
        } catch (IdException e) {
            return "Id not found";
        }
    }

    @Override
    public void setArgs(Reader from) {
        id = from.readInt(BigInteger.ZERO, BigInteger.valueOf(Integer.MAX_VALUE)).longValue();
        arg = from.readObject();
    }

    @Override
    public String toString() {
        String res = "update";
        if (currentScripts.size() != 0) {
            res += "(in " + currentScripts.get(currentScripts.size() - 1) + " script)";
        }
        return res;
    }
}
