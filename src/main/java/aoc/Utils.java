package aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utility methods for AOC problems.
 */
public class Utils
{
    /**
     * Get all lines from a resource file as a list of strings.
     *
     * @param resourceName The name of the resource with input data
     * @return A list of strings representing the lines in the file
     * @throws URISyntaxException if this URL is not formatted strictly according to RFC2396 and cannot be converted to a URI.
     * @throws IOException        if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
     */
    public static List<String> getInput(String resourceName) throws URISyntaxException, IOException
    {
        URL url = Utils.class.getClassLoader().getResource(resourceName);
        return Files.readAllLines(Paths.get(url.toURI()));
    }
}
