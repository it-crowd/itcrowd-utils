package pl.com.it_crowd.utils.test;

import java.io.File;

public final class LibraryResolver {
// ------------------------------ FIELDS ------------------------------

    public static final String DEFAULT_LIBRARY_DIRECTORY = "target/test-libs";

    public static final String LIBRARY_DIRECTORY_PROPERTY = "test.library.dir";

// -------------------------- STATIC METHODS --------------------------

    private static File getLibraryDirectory()
    {
        final String directoryName = System.getProperty(LIBRARY_DIRECTORY_PROPERTY, DEFAULT_LIBRARY_DIRECTORY);
        final File file = new File(directoryName);
        if (!file.isDirectory()) {
            throw new IllegalStateException(String.format("%s does not exist or is not a directory", file.getAbsolutePath()));
        }
        return file;
    }

    public static File resolve(String resourceName)
    {
        return new File(getLibraryDirectory(), resourceName + (resourceName.endsWith(".jar") ? "" : ".jar"));
    }

// --------------------------- CONSTRUCTORS ---------------------------

    private LibraryResolver()
    {
    }
}
