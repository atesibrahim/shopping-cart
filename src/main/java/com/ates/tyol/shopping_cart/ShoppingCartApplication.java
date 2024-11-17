package com.ates.tyol.shopping_cart;

import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.service.file.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class ShoppingCartApplication {
	private static final String FILE_PATH_RECEIVED = "File path received: ";
	private final FileServiceImpl fileService;

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			if (args.length > 0) {
				System.out.println(FILE_PATH_RECEIVED + args[0]);
				performTask(args[0]);
			} else {
				System.out.println(ErrorMessageConstants.FILE_PATH_NOT_PROVIDED);
			}
		};
	}

	private void performTask(String argument) {
		String result = fileService.execute(argument);
		System.out.println(result);
	}
}
