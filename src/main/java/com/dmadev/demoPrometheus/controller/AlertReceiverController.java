package com.dmadev.demoPrometheus.controller;

import com.dmadev.demoPrometheus.dto.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AlertReceiverController {


    @PostMapping("/receiveAlert")
    public void receiveAlert(@RequestBody Alert alert){
        log.info("Received alert: {}", alert);


        //перенести в другое приложение
        /*

        * todo  еще одну аппку, которая будет принимать твой алерт по ресту и пока что просто
        *  логгировать и ложить в отдельную базу. можешь использовать тот же инстанс постгреса, но дуругую базу
        * */
    }
}
