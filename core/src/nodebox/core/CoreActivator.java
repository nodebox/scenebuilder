package nodebox.core;

import nodebox.node.NodeManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class CoreActivator implements BundleActivator {
    private ServiceRegistration nodeManagerRegistration;

    public void start(BundleContext context) throws Exception {
        nodeManagerRegistration = context.registerService(NodeManager.class.getName(), new NodeManager(), null);
    }

    public void stop(BundleContext context) throws Exception {
        nodeManagerRegistration.unregister();
    }
}
