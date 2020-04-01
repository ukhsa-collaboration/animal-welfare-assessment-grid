package uk.gov.phe.erdst.sc.awag.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public final class GlassfishTestsHelper
{
    private static final String APP_NAME_SYS_PROPERTY = "awProjectName";
    private static final String GLASSFISH_INSTALL_ROOT_SYS_PROPERTY = "glassfishInstallationRoot";
    private static final String ECLIPSE_IDE_DEBUGGING_ENABLED = "eclipseIdeDebuggingEnabled";
    private static final String DEFAULT_GLASSFISH_INSTALL_ROOT = System.getenv("PAYARA_GLASSFISH_ROOT");
    private static final String GLASSFISH_DOMAIN_NAME = "awag-integration-tests";
    private static final String GLASSFISH_DOMAIN_PATH = "/domains/" + GLASSFISH_DOMAIN_NAME;

    private static String sClassLookupLocation;
    private static EJBContainer sContainer;
    private static Context sContext;

    private GlassfishTestsHelper()
    {
    }

    public static void preTestSetup()
    {
        if (System.getProperty(ECLIPSE_IDE_DEBUGGING_ENABLED) != null)
        {
            System.setProperty(APP_NAME_SYS_PROPERTY, EclipseIdeIntegrationTestsConstants.ECLIPSE_IDE_DEFAULT_APP_NAME);
            System.setProperty(GLASSFISH_INSTALL_ROOT_SYS_PROPERTY, DEFAULT_GLASSFISH_INSTALL_ROOT);
        }

        if (System.getProperty(GLASSFISH_INSTALL_ROOT_SYS_PROPERTY) == null)
        {
            throw new RuntimeException(GLASSFISH_INSTALL_ROOT_SYS_PROPERTY + " Java VM property is not set");
        }

        sClassLookupLocation = "java:global/" + System.getProperty(APP_NAME_SYS_PROPERTY) + "/classes/";

        setLoggingLevel(Level.SEVERE);

        Map<String, Object> properties = getContainerProperties();

        sContainer = EJBContainer.createEJBContainer(properties);
        sContext = sContainer.getContext();
    }

    public static void switchInfoLevelLogging()
    {
        setLoggingLevel(Level.INFO);
    }

    private static void setLoggingLevel(Level loggingLevel)
    {
        Logger.getLogger("").getHandlers()[0].setLevel(loggingLevel);
        Logger.getLogger("javax.enterprise.system.tools.deployment").setLevel(loggingLevel);
        Logger.getLogger("javax.enterprise.system").setLevel(loggingLevel);
    }

    private static Map<String, Object> getContainerProperties()
    {
        Map<String, Object> properties = new HashMap<String, Object>();

        // TODO Required? During manual debug?
        File[] modules = {new File("target/test-classes"), new File("target/classes")};
        properties.put(EJBContainer.MODULES, modules);
        // System.out.print(System.getProperty("user.dir"));

        properties.put(EJBContainer.APP_NAME, System.getProperty(APP_NAME_SYS_PROPERTY));
        properties.put("org.glassfish.ejb.embedded.glassfish.installation.root",
            System.getProperty(GLASSFISH_INSTALL_ROOT_SYS_PROPERTY));

        final String installationRoot = DEFAULT_GLASSFISH_INSTALL_ROOT + GLASSFISH_DOMAIN_PATH;
        properties.put("org.glassfish.ejb.embedded.glassfish.instance.root", installationRoot);

        return properties;
    }

    public static void onTestFinished()
    {
        if (sContainer == null)
        {
            throw new IllegalStateException("EJBContainer has not been initialised! Run preTestSetup() first");
        }

        sContainer.close();
    }

    private static String getLookupPath(String className)
    {
        return sClassLookupLocation + className;
    }

    public static Object lookup(String className) throws NamingException
    {
        return sContext.lookup(getLookupPath(className));
    }

    public static Object lookupMultiInterface(String className, Class<?> interfaceClass) throws NamingException
    {
        return sContext.lookup(getLookupPath(className) + "!" + interfaceClass.getName());
    }

    public static void replaceBinding(String className, Object object) throws NamingException
    {
        NamingEnumeration<Binding> bindings = sContext.listBindings(sContext.getNameInNamespace());
        // sContext.unbind(className);
        // sContext.rebind(getLookupPath(className), object);
        // sContext.unbind(name);
        sContext.bind(className, object);
    }
}
