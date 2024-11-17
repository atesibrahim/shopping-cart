package com.ates.tyol.shopping_cart.service.file;

import com.ates.tyol.shopping_cart.event.Event;
import com.ates.tyol.shopping_cart.service.cart.CartService;
import com.ates.tyol.shopping_cart.service.factory.EventFactoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final String OUTPUT_FILE = "/output";
    private final CartService cartService;
    private final EventFactoryServiceImpl eventFactoryServiceImpl;

    @Override
    public String execute(String filePath) {
        try {
            return processInputFile(filePath);
        } catch (IOException e) {
            System.out.println("Error reading the file: "+ e.getMessage());
            return "{\"result\":false, \"message\":\"" + e.getMessage() + " \"}";
        }
    }

    private String processInputFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            return executeFileContent(path);
        } else {
            System.out.println("File not found: Path: " + path.toAbsolutePath());
            throw new IOException("File not found: Path: " + path.toAbsolutePath());
        }
    }

    private String executeFileContent(Path path) throws IOException {
        Path outputPath = createOutputFile(path);
        List<String> lines = Files.readAllLines(path);
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            String result;
            try {
                Event event = eventFactoryServiceImpl.executeEvent(line);
                result = event.execute(cartService);
            } catch (Exception e) {
                result = "{\"result\":false, \"message\":\"" + e.getMessage() + " \"}";
            }
            Files.writeString(outputPath, result + "\n", StandardOpenOption.APPEND);
            stringBuilder.append(result).append("\n");
        }
        return stringBuilder.toString();
    }

    private Path createOutputFile(Path path) throws IOException {
        Path fileParent = path.getParent();
        String fileExtension = path.toString().substring(path.toString().lastIndexOf("."));
        String outputFilePath = fileParent + OUTPUT_FILE + fileExtension;
        Path outputPath = Paths.get(outputFilePath);
        Files.createDirectories(fileParent);
        if (!Files.exists(outputPath)) {
            Files.createFile(outputPath);
        }
        return outputPath;
    }
}
