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
     * Метод для генерации указанного количества фейковых сотрудников и сохранения их в базе данных.
     *
     * @param count количество сотрудников для генерации
     */
    public void generateFakeEmployees(int count) {
        for (int i = 0; i < count; i++) {
            buildRandomEmployee();
        }
    }


    /**
     * Приватный метод для создания случайного объекта Employee.
     * Генерирует случайные значения для имени, фамилии, даты найма и зарплаты.
     *
     * @return сгенерированный объект Employee
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
     * Генерирует случайную строку длиной от minLength до maxLength символов.
     *
     * @param minLength минимальная длина строки
     * @param maxLength максимальная длина строки
     * @return сгенерированная случайная строка
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
     * Метод для начала генерации фейковых сотрудников с интервалом в 5 секунд.
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
     * Метод для остановки генерации фейковых данных
     */
    public void stopGenerationEmployees() {
        isGenerating.set(false);
    }


    /**
     * Проверяет, запущен ли процесс генерации сотрудников.
     *
     * @return true, если процесс генерации запущен, иначе false
     */
    public boolean isGeneratingEmployees() {
        return isGenerating.get();
    }

}
