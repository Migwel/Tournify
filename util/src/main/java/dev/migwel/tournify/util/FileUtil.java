package dev.migwel.tournify.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileUtil {

    private FileUtil() {
        //Util
    }

    public static String loadFile(String filename) throws URISyntaxException, IOException {
        URL fileUrl = FileUtil.class.getClassLoader().getResource(filename);
        if (fileUrl == null) {
            throw new IOException("File could not be found: "+ filename);
        }
        Path path = Paths.get(fileUrl.toURI());
        Stream<String> lines = Files.lines(path);
        String json = lines.collect(Collectors.joining("\n"));
        lines.close();
        return json;
    }
}
