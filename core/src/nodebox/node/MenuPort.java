package nodebox.node;

import java.util.Map;

public interface MenuPort {
    public boolean hasMenu();

    public Map<String, String> getMenuItems();
}
