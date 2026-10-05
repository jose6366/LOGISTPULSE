package com.usfq.logistpulse.config;
import com.usfq.logistpulse.dto.CreateOrderRequest;
import com.usfq.logistpulse.repository.OrderRepository;
import com.usfq.logistpulse.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
@Configuration
public class DataSeeder { @Bean CommandLineRunner seedLogistpulse(OrderRepository repo,OrderService service){return args->{if(repo.count()==0){service.create(new CreateOrderRequest("Restaurante Centro","Av. Demo 123","Insumo A",4),"system");service.create(new CreateOrderRequest("Sucursal Norte","Calle Ejemplo 456","Insumo B",2),"system");}};} }
