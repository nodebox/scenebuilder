package nodebox.node;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The category provides a taxonomy for nodes.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Category {
    String value();
}
