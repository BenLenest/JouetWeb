package tools;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Singleton class loader used to load jar services.
 */
public class JarLoader {

    /* CONSTANTS ========================================================== */

    public static final String JAR_PREFIX = "jar:file:";
    public static final String JAR_SUFFIX = "!/";

    /* ATTRIBUTES ========================================================== */

    private final URLClassLoader classLoader;

    /* INSTANCE ============================================================ */

    private JarLoader(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private static JarLoader INSTANCE = new JarLoader(initializeClassLoader());

    public static JarLoader getInstance() {
        return INSTANCE;
    }

    /* GETTERS ========================================================== */

    public URLClassLoader getClassLoader() {
        return classLoader;
    }

    /* PRIVATE STATIC METHODS ============================================== */

    private static URLClassLoader initializeClassLoader() {
        String servicesDirectory = System.getProperty("user.dir") + "/services/";
        File[] files = new File(servicesDirectory).listFiles();
        URL[] urls = new URL[files.length];
        for (int i = 0 ; i < files.length ; i++) {
            try {
                urls[i] = new URL(JarLoader.JAR_PREFIX + servicesDirectory + files[i].getName() + JAR_SUFFIX);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return new URLClassLoader(urls, JarLoader.class.getClassLoader());
    }
}
