package com.dmadev.demoPrometheus.controller;

import com.dmadev.demoPrometheus.service.FakeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fake-data")
@RequiredArgsConstructor
public class FakeDataController {
    private final FakeDataService fakeDataService;


    @PostMapping("/generate-employees")
    public String handleGenerateFakeEmployees(@RequestParam int count) {
        fakeDataService.generateFakeEmployees(count);
        return count + " fake employees generated.";
    }

   @PostMapping("/auto-generate-employees")
   public String generateEmployees(@RequestParam int action){
        if(action==1){
            fakeDataService.startGeneratingEmployees();
            return "Employee generation started";
        }else if(action==0){
                fakeDataService.stopGenerationEmployees();
                return "Employee generation stopped";
        }else{
            return "Invalid action parameter. Use '1' to start or '0' to stop generation";
        }
   }
   @GetMapping("/is-generating")
   public boolean isGenerating(){
        return fakeDataService.isGeneratingEmployees();
   }
}
