package mx.com.leyva;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderProcessingServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessingServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderProcessingServiceApplication.class, args);
        log.info("Microservicio - Procesamiento Órdenes - Iniciado correctamente!.");
    }

}
