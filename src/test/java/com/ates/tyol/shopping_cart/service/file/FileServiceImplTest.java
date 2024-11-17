package com.ates.tyol.shopping_cart.service.file;

import com.ates.tyol.shopping_cart.event.Event;
import com.ates.tyol.shopping_cart.service.cart.CartService;
import com.ates.tyol.shopping_cart.service.factory.EventFactoryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private CartService cartService;

    @Mock
    private EventFactoryServiceImpl eventFactoryServiceImpl;

    @InjectMocks
    private FileServiceImpl cartCommandService;

    private Path tempInputFile;

    @BeforeEach
    void setup() throws IOException {
        // Create a temporary file to simulate input
        tempInputFile = Files.createTempFile("input", ".txt");
    }

    @AfterEach
    void cleanup() throws IOException {
        // Clean up temporary files
        if (tempInputFile != null) {
            Files.deleteIfExists(tempInputFile);
        }
    }

    @Test
    void execute_thenReturnExpectedResult() throws IOException {
        // Given
        String commandLine = """
                {"command":"addItem", "payload":{"itemId":4, "categoryId":3003, "sellerId":2001, "price":75.0, "quantity":1}}
                """;
        String commandResult = "{\"result\":true, \"message\":\"Item added successfully\"}";

        Files.writeString(tempInputFile, commandLine);
        Path outputPath = tempInputFile.getParent().resolve("output.txt");

        Event command = Mockito.mock(Event.class);
        Mockito.when(eventFactoryServiceImpl.executeEvent(commandLine)).thenReturn(command);
        Mockito.when(command.execute(cartService)).thenReturn(commandResult);

        // When
        cartCommandService.execute(tempInputFile.toString());

        // Then
        assertTrue(Files.exists(outputPath), "Output file should be created");
    }

    @Test
    void execute_whenFileNotFound_thenReturnErrorMessage() {
        // Given
        String invalidPath = tempInputFile.getParent().resolve("nonexistent.txt").toString();

        // When
        String result = cartCommandService.execute(invalidPath);

        // Then
        assertTrue(result.contains("File not found"), "Result should indicate file not found");
    }

    @Test
    void execute_whenCommandExecutionFails_thenHandleException() throws IOException {
        // Given
        String commandLine = "INVALID_COMMAND";
        String errorMessage = "Invalid command syntax";

        Files.writeString(tempInputFile, commandLine);
        Path outputPath = tempInputFile.getParent().resolve("output.txt");

        Mockito.when(eventFactoryServiceImpl.executeEvent(commandLine))
                .thenThrow(new RuntimeException(errorMessage));

        // When
        cartCommandService.execute(tempInputFile.toString());

        // Then
        assertTrue(Files.exists(outputPath), "Output file should be created");
        String outputContent = Files.readString(outputPath);
        assertTrue(outputContent.contains(errorMessage), "Output file should contain error message");
    }

    @Test
    void execute_whenFileWritingFails_thenHandleException() throws IOException {
        // Given
        String commandLine = "{\"command\":\"addItem\", \"payload\":{\"itemId\":4, \"categoryId\":3003, \"sellerId\":2001, \"price\":75.0, \"quantity\":1}}";
        String commandResult = "{\"result\":false, \"message\":\"Item added successfully\"}";

        Files.writeString(tempInputFile, commandLine);
        Path outputPath = tempInputFile.getParent().resolve("output.txt");

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.writeString(outputPath, commandResult + "\n", StandardOpenOption.APPEND))
                    .thenThrow(new IOException("Write failed"));

            // When & Then
            String result = cartCommandService.execute(tempInputFile.toString());
            assertTrue(result.contains("File not found: Path"));
        }
    }

    @Test
    void createOutputFile_whenOutputFileDoesNotExist_thenCreateItSuccessfully() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        Path outputPath = tempInputFile.getParent().resolve("output.txt");

        // When
        Method createOutputFileMethod = FileServiceImpl.class.getDeclaredMethod("createOutputFile", Path.class);
        createOutputFileMethod.setAccessible(true);
        Path createdPath = (Path) createOutputFileMethod.invoke(cartCommandService, tempInputFile);

        // Then
        assertTrue(Files.exists(createdPath), "Output file should be created");
        assertEquals(outputPath, createdPath, "Output file path should match expected");
    }
}
