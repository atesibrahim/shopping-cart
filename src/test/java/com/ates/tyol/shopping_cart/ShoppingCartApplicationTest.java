package com.ates.tyol.shopping_cart;

import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.service.file.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ShoppingCartApplicationTest {

    @Autowired
    private ShoppingCartApplication shoppingCartApplication;

    @MockBean
    private FileServiceImpl mockCartCommandService;

    @Test
    void run_whenCommandLineArgumentProvided_thenPerformTask() throws Exception {
        String argument = "some-command";
        CommandLineRunner commandLineRunner = shoppingCartApplication.run();
        when(mockCartCommandService.execute(argument)).thenReturn("some-result");

        commandLineRunner.run(argument);

        verify(mockCartCommandService, times(1)).execute(argument);
    }

    @Test
    void run_whenNoCommandLineArgumentProvided_thenPrintNoArgumentsMessage() throws Exception {
        CommandLineRunner commandLineRunner = shoppingCartApplication.run();
        PrintStream originalSystemOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        commandLineRunner.run();

        assertTrue(baos.toString().contains(ErrorMessageConstants.FILE_PATH_NOT_PROVIDED));
        System.setOut(originalSystemOut);
    }
}
