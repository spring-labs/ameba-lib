package ch.abraxas.system;

/**
 * Created by axbsi01 on 17.06.2015.
 */
public class SystemSupport {

    private SystemSupport(){
    }

    /**
     * True if the current operating system is windows
     */
    public static final boolean IS_OS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("win");

    /**
     * Path to the java temp directory as string
     */
    public static final String JAVA_TMP_DIR = System.getProperty("java.io.tmpdir");

}
