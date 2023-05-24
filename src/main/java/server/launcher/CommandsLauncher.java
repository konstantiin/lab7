package server.launcher;


import common.exceptions.inputExceptions.IdException;
import common.storedClasses.Coordinates;
import common.storedClasses.HumanBeing;
import server.main.Main;

import java.io.File;
import java.util.*;


/**
 * realizes all the commands
 *
 * @param <T> - stored class
 */
public class CommandsLauncher<T extends Comparable<T>> {
    /**
     * scripts that are currently executing
     */
    public static ArrayList<File> currentScripts = new ArrayList<>();
    /**
     * managed collection
     */
    private final TreeSet<T> collection;

    /**
     * @param collection - managed collection
     */
    public CommandsLauncher(TreeSet<T> collection) {
        this.collection = collection;
    }

    /**
     * adds element to collection
     *
     * @param element - element to add
     */
    @SuppressWarnings("unchecked")
    public Boolean add(Object element) {
        return collection.add((T) element);
    }

    /**
     * adds element if it is less than any element in collection
     *
     * @param element - element to add
     * @return true if element was added
     */
    @SuppressWarnings("unchecked")

    public boolean addIfMin(Object element) {
        T value = (T) element;
        if (value.compareTo(collection.first()) < 0) {
            this.add(value);
            return true;
        }
        return false;

    }

    /**
     * returns elements with given substring in their names
     *
     * @param pattern - substring to search in names
     * @return list of elements with give substring in names
     */
    public Object[] filterContainsName(String pattern) {

        return collection.stream().filter(h ->
                ((HumanBeing) h).getName().contains(pattern)).toArray();
    }

    /**
     * groups HumanBeings by coordinates
     *
     * @return HumanBeings grouped by coordinates
     */
    public HashMap<Coordinates, List<HumanBeing>> groupCountingByCoordinates() {
        HashMap<Coordinates, List<HumanBeing>> groups = new HashMap<>();

        collection.forEach(h -> {
            var human = (HumanBeing) h;
            if (groups.containsKey(human.getCoordinates())) {
                groups.get(human.getCoordinates()).add(human);
            } else {
                var value = new ArrayList<HumanBeing>();
                value.add(human);
                groups.put(human.getCoordinates(), value);
            }
        });
        return groups;
    }

    /**
     * prints information about collection
     */
    public String info() {
        return "TreeSet " + collection + " of size " + collection.size();// возможно стоит вывести еще какую-то информацию
    }

    /**
     * Deletes element with given id
     *
     * @param id - element with this id will be removed
     * @throws IdException - if element with id does not exist
     */
    public void removeById(long id) throws IdException {
        long c = collection.stream().filter(h -> ((HumanBeing) h).getId() == id).count();
        if (c == 0) throw new IdException("Not valid id");
        collection.stream().filter(h -> ((HumanBeing) h).getId() == id).forEach(collection::remove);

    }

    /**
     * removes all the elements that are less than given element
     *
     * @param element - element to compare
     */
    @SuppressWarnings("unchecked")

    public void removeLower(Object element) {
        T value = (T) element;
        collection.stream().filter(h -> h.compareTo(value) < 0).forEach(h -> {
            HumanBeing.ids.remove(((HumanBeing) h).getId());
            collection.remove(h);
        });
    }

    /**
     * removes all the elements that are greater than given element
     *
     * @param element - element to compare
     */
    @SuppressWarnings("unchecked")

    public void removeGreater(Object element) {
        T value = (T) element;
        collection.stream().filter(h -> h.compareTo(value) > 0).forEach(h -> {
            HumanBeing.ids.remove(((HumanBeing) h).getId());
            collection.remove(h);
        });

    }

    /**
     * shows elements of collection
     */
    public List<Object> show() {
        return new ArrayList<>(collection);
    }

    /**
     * @return sum of impactSpeed
     */
    public double sumOfImpactSpeed() {
        Float sum = (float) 0;
        final ArrayList<Float> arr = new ArrayList<>();
        collection.forEach((e) -> arr.add(((HumanBeing) e).getImpactSpeed()));
        return (double) arr.stream()
                .reduce(sum, Float::sum);
    }

    /**
     * updates element with given id
     *
     * @param id      element id
     * @param element new element
     * @throws IdException - if element with id does not exist
     */
    public void update(long id, Object element) throws IdException {
        long c = collection.stream().filter(h -> ((HumanBeing) h).getId() == id).count();
        if (c == 0) throw new IdException("Not valid id");
        collection.stream().filter(h -> ((HumanBeing) h).getId() == id).forEach(h -> ((HumanBeing) h).update((HumanBeing) element));

    }

    /**
     * clears collection
     */
    public void clear() {
        HumanBeing.ids.clear();
        collection.clear();
    }

    /**
     * saves collection
     */
    @SuppressWarnings("unchecked")
    public void save() {
        Main.XMLInput.writeArr(new ArrayList<>((Collection<HumanBeing>) collection));
    }

    public boolean runServerCommand(String command) {
        if (command.equals("save")) {
            this.save();
            System.out.println("Collection saved");
            return true;
        }
        if (command.equals("interrupt")) {
            this.save();
            System.out.println("Program interrupted");
            return false;
        }

        System.out.println("Unknown command.");
        return true;
    }
}
