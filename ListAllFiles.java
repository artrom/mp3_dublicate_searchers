import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ListAllFiles {
    void listAllFiles(Path currentPath, List<Path> allFiles)
            throws IOException
    {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath))
        {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    listAllFiles(entry, allFiles);
                } else {
                    allFiles.add(entry);
                }
            }
        }
    }
}
