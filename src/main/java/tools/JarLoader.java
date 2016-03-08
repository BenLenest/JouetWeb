package tools;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public final class JarLoader {

    /* CONSTANTS ========================================================== */

    public static final String JAR_PREFIX = "jar:file:";
    public static final String JAR_SUFFIX = "!/";

    /* ATTRIBUTES ========================================================== */

    private final Map<String, URLClassLoader> classLoaders;

    /* INSTANCE ============================================================ */

    private JarLoader(Map<String, URLClassLoader> classLoaders) {
        this.classLoaders = classLoaders;
    }

    private static JarLoader INSTANCE = new JarLoader(initializeClassLoader());

    public static JarLoader getInstance() {
        return INSTANCE;
    }

    /* GETTERS ========================================================== */

    public Map<String, URLClassLoader> getClassLoaders() {
        return classLoaders;
    }

    /* PRIVATE STATIC METHODS ============================================== */

    private static Map<String, URLClassLoader> initializeClassLoader() {
        Map<String, URLClassLoader> classLoaders = new HashMap<>();
        String servicesDirectory = System.getProperty("user.dir") + "/services/";
        File[] files = new File(servicesDirectory).listFiles();
        for (int i = 0 ; i < files.length ; i++) {
            try {
                URL[] url = new URL[1];
                url[0] = new URL(JAR_PREFIX + servicesDirectory + files[i].getName() + JAR_SUFFIX);
                classLoaders.put(files[i].getName(), new URLClassLoader(url, JarLoader.class.getClassLoader()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return classLoaders;
    }

}
