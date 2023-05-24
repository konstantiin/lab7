package server.parse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.security.AnyTypePermission;
import common.exceptions.fileExceptions.FIleDoesNotExistException;
import common.exceptions.fileExceptions.FileNotReadableException;
import common.exceptions.fileExceptions.FileNotWritableException;
import common.exceptions.inputExceptions.InputException;
import common.storedClasses.HumanBeing;
import common.storedClasses.forms.HumanBeingForm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


/**
 * xml parser class
 */
public class ParseXml {
    /**
     * parsed file
     */
    private final File xml;
    /**
     * XStream stream for parsing file
     */
    private final XStream stream;

    /**
     * @param path - file path
     * @throws FIleDoesNotExistException - if file does not exist
     * @throws FileNotReadableException  - if file can't be read
     * @throws FileNotWritableException  - if file can't be modified
     */
    public ParseXml(String path) throws FIleDoesNotExistException, FileNotReadableException, FileNotWritableException {

        xml = new File(path);
        if (!xml.exists()) throw new FIleDoesNotExistException();
        if (!xml.canRead()) throw new FileNotReadableException();
        if (!xml.canWrite()) throw new FileNotWritableException();
        stream = new XStream();


        stream.addPermission(AnyTypePermission.ANY);

        stream.alias("elements", List.class);
    }

    /**
     * return parser to read file
     *
     * @param path - file  path
     * @return xml parser
     */
    public static ParseXml getXMLInput(String path) {
        while (true) {
            try {
                return new ParseXml(path);
            } catch (FIleDoesNotExistException e) {
                System.out.println("Your file does not exist! Type correct file path");
                path = new Scanner(System.in).next();
            } catch (FileNotReadableException e) {
                System.out.println("Your file can't be read! Type correct file path");
                path = new Scanner(System.in).next();
            } catch (FileNotWritableException e) {
                System.out.println("Your file isn't writable! Type correct file path");
                path = new Scanner(System.in).next();
            } catch (NullPointerException e) {
                System.out.println("File name hasn't been read! Please type it again");
                path = new Scanner(System.in).next();
            }
        }
    }

    /**
     * writes List of HumanBeings in file
     *
     * @param out - List to write in file
     */
    public void writeArr(List<HumanBeing> out) {
        stream.processAnnotations(HumanBeing.class);
        String result = stream.toXML(out);
        try (FileOutputStream o = new FileOutputStream(xml)) {
            o.write(result.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * reads file, returns array of elements
     *
     * @return array of HumanBeings
     */
    @SuppressWarnings("unchecked")
    public List<HumanBeing> getArr() {
        stream.processAnnotations(HumanBeingForm.class);
        List<HumanBeingForm> beings;
        try {
            beings = (List<HumanBeingForm>) stream.fromXML(xml);// хз, что делать
        } catch (ConversionException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                System.out.print("Collection was left empty, because of wrong input in line ");
                var message = e.getMessage().split("\\s+");
                for (int i = 0; i < message.length; i++) {
                    if (Objects.equals(message[i], "number")) {
                        System.out.println(message[i + 2]);
                        return null;
                    }
                }
                System.out.println();
            } else {
                System.out.println("File is not correct. Collection was left empty");
            }
            return null;
        } catch (StreamException | CannotResolveClassException e) {
            System.out.println("File is not correct. Collection was left empty");
            return null;
        }
        List<HumanBeing> result = new ArrayList<>();
        int count = 1;
        for (HumanBeingForm i : beings) {
            try {
                result.add(new HumanBeing(i));
            } catch (InputException e) {
                System.out.println("Element " + count + " is not correct, because " + e.getMessage() + " It was skipped");
            }
            count++;
        }

        return result;
    }
}