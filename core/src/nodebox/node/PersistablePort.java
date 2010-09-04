package nodebox.node;

/**
 * This interface adds method to the port for saving and loading the port.
 */
public interface PersistablePort {

    /**
     * Parse the given string value and return a value that can be used with setValue.
     * <p/>
     * If the value is inappropriate or cannot be parsed, throw an IllegalArgumentException.
     *
     * @param value the string value to parse
     * @return a value in the correct format.
     * @throws IllegalArgumentException if the value cannot be parsed.
     */
    public Object parseValue(String value) throws IllegalArgumentException;

    /**
     * Return the current port value as a string, ready to be serialized.
     * The returned string will be used with parseValue when loading a scene file.
     *
     * @return the value as a string representation.
     */
    public String getValueAsString();

}
