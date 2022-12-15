package br.ufsm.csi.pilacoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "br.ufsm.csi.pilacoin")
@EntityScan("br.ufsm.csi.pilacoin")
public class PilaCoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(PilaCoinApplication.class, args);
	}

}
