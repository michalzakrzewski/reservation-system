package com.zakrzewski.reservationsystem.init;

import com.zakrzewski.reservationsystem.enums.TeamEnum;
import com.zakrzewski.reservationsystem.model.entity.EmployeeEntity;
import com.zakrzewski.reservationsystem.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializeRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DataInitializeRunner.class);

    private final EmployeeRepository employeeRepository;

    @SuppressWarnings("unused")
    public DataInitializeRunner() {
        this(null);
    }

    @Autowired
    public DataInitializeRunner(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(final String... args) throws Exception {
        LOG.info("Initializing data...");
        final EmployeeEntity alphaDeveloper = EmployeeEntity.builder()
                .withFirstName("Jan")
                .withLastName("Kowalski")
                .withEmail("jan.kowalski@firma.pl")
                .withTeam(TeamEnum.ALPHA)
                .build();
        employeeRepository.save(alphaDeveloper);
        LOG.info("New alphaDeveloper has been saved successfully = {}", alphaDeveloper);

        final EmployeeEntity omegaDeveloper = EmployeeEntity.builder()
                .withFirstName("Anna")
                .withLastName("Nowak")
                .withEmail("anna.nowak@firma.pl")
                .withTeam(TeamEnum.OMEGA)
                .build();
        employeeRepository.save(omegaDeveloper);
        LOG.info("New omegaDeveloper has been saved successfully = {}", omegaDeveloper);

        final EmployeeEntity admin = EmployeeEntity.builder()
                .withFirstName("Piotr")
                .withLastName("Wójcik")
                .withEmail("piotr.wojcik@firma.pl")
                .withTeam(TeamEnum.ADMIN)
                .build();
        employeeRepository.save(admin);
        LOG.info("New admin has been saved successfully = {}", admin);

        final EmployeeEntity tester = EmployeeEntity.builder()
                .withFirstName("Katarzyna")
                .withLastName("Zielińska")
                .withEmail("katarzyna.zielinska@firma.pl")
                .withTeam(TeamEnum.TESTER)
                .build();
        employeeRepository.save(tester);
        LOG.info("New tester has been saved successfully = {}", tester);

        final EmployeeEntity accountManager = EmployeeEntity.builder()
                .withFirstName("Tomasz")
                .withLastName("Lewandowski")
                .withEmail("tomasz.lewandowski@firma.pl")
                .withTeam(TeamEnum.ACCOUNT_MANAGER)
                .build();
        employeeRepository.save(accountManager);
        LOG.info("New accountManager has been saved successfully = {}", accountManager);
    }
}
