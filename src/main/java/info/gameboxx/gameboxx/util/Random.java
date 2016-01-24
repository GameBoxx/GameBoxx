package info.gameboxx.gameboxx.util;

import java.util.List;

public class Random {

    private static java.util.Random random;

    static {
        random = new java.util.Random();
    }

    /**
     * Get a random number between start and end.
     * @param start
     * @param end
     * @return random int
     */
    public static int Int(int start, int end) {
        return start + random.nextInt(end - start + 1);
    }

    /**
     * Get a random Integer between 0 and the number specified.
     * @param end
     * @return random int
     */
    public static int Int(int end) {
        return random.nextInt(end);
    }

    /**
     * Get a random float number between start and end.
     * @param start
     * @param end
     * @return random float
     */
    public static float Float(float start, float end) {
        return random.nextFloat() * (end - start) + start;
    }

    /**
     * Get a random float (Same as Random.nextFloat())
     * @return random float between 0-1
     */
    public static float Float() {
        return random.nextFloat();
    }

    /**
     * Get a random double number between start and end.
     * @param start
     * @param end
     * @return random double
     */
    public static double Double(double start, double end) {
        return random.nextDouble() * (end - start) + start;
    }

    /**
     * Get a random double (Same as Random.nextDouble())
     * @return random double between 0-1
     */
    public static double Double() {
        return random.nextDouble();
    }

    /**
     * Get a random value out of a Array.
     * @param array The array like String[] or int[]
     * @return Random value out of array.
     */
    public static <T> T Item(T[] array) {
        return array[Int(array.length-1)];
    }

    /**
     * Get a random value out of a List.
     * @param list The list like List<String>
     * @return Random value out of list.
     */
    public static <T> T Item(List<T> list) {
        return list.get(Int(list.size() - 1));
    }

    /**
     * Get the raw random instance from java.util.
     * @return Random instance from java.util.
     */
    public static java.util.Random raw() {
        return random;
    }

}
