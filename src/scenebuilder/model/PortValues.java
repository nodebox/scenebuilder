package scenebuilder.model;

import java.util.HashMap;
import java.util.Map;

public class PortValues {

    private static ThreadLocal<Map<Port, Object>> values = new ThreadLocal<Map<Port, Object>>() {
        @Override
        protected synchronized Map<Port, Object> initialValue() {
            return new HashMap<Port, Object>();
        }
    };

    public static void setPortValue(Port port, Object value) {
        values.get().put(port, value);
    }

    public static Object getPortValue(Port port) {
        return values.get().get(port);
    }

}
