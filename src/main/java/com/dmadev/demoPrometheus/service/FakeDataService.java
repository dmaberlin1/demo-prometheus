package com.dmadev.demoPrometheus.service;

import com.dmadev.demoPrometheus.entity.Employee;
import com.dmadev.demoPrometheus.repository.EmployeeRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@RequiredArgsConstructor
public class FakeDataService {
    private final EmployeeRepository employeeRepository;
    private final Random random=new Random();
    Faker faker = new Faker();
    private final AtomicBoolean isGenerating = new AtomicBoolean(false);



    /**
     * A method for generating a specified number of fake employees and storing them in a database.
     *
     * @param count number of employees for generation
     */
    public void generateFakeEmployees(int count) {
        for (int i = 0; i < count; i++) {
            buildRandomEmployee();
        }
    }


    /**
     *  Private method to create a random Employee object.
     * Generates random values for first name, last name, hire date, and salary.
     *
     * @return generated object  Employee
     */
    private void buildRandomEmployee() {
        Employee employee = Employee.builder()
                .firstName(generateRandomString(2, 9))
                .lastName(generateRandomString(3, 15))
                .hireDate(LocalDateTime.now().minusDays(random.nextInt(365)))
                .salary(BigDecimal.valueOf(30000 + random.nextInt(70000)))
                .build();
        employeeRepository.save(employee);
    }


    /**
     * Generates a random string of length from minLength to maxLength characters.
     *
     * @param minLength minimum string length
     * @param maxLength maximum string length
     * @return generated random string
     */
    private String generateRandomString(int minLength, int maxLength) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int length = random.nextInt((maxLength - minLength) + 1) + minLength;
        var builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            builder.append(characters.charAt(index));
        }
        return builder.toString();
    }

    /**
     * Method to start generating fake employees at 5 second intervals.
     */
    @Async
    public void startGeneratingEmployees() {
        if (!isGenerating.compareAndSet(false, true)) {
            log.warn("Generation process is already running");
            return;
        }

        log.info("Starting employee generation process.");

        while (isGenerating.get()) {
            buildRandomEmployee();
            try {
                Thread.sleep(5000);//5 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread interrupted while waiting", e);
            }
        }

        log.info("Stopping employee generation process");
    }


    /**
     * Method for stopping the generation of fake data
     */
    public void stopGenerationEmployees() {
        isGenerating.set(false);
    }


    /**
     * Checks if the employee generation process is running.
     *
     * @return true if the generation process is running, otherwise false
     */
    public boolean isGeneratingEmployees() {
        return isGenerating.get();
    }

}
