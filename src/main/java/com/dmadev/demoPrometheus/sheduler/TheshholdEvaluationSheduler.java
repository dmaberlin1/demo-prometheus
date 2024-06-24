package com.dmadev.demoPrometheus.sheduler;

import com.dmadev.demoPrometheus.service.DatabaseMetricsService;
import com.dmadev.demoPrometheus.service.ThresholdEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public  class TheshholdEvaluationSheduler {
    private final ThresholdEvaluationService thresholdEvaluationService;


    @Scheduled(fixedRate = 30_000)  // 30 sec - for development
    private void getEvaluation(){
        thresholdEvaluationService.evaluateAndGenerateAlert();
    }
}
