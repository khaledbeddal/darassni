package com.esi.mscours.proxy;

import com.esi.mscours.DTO.PayLecturesDto;
import com.esi.mscours.model.Debited;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(name="ms-payment")
@LoadBalancerClient(name = "ms-payment")

public interface PaymentProxy {


    @GetMapping("/debiteds/search/findDebitedsByIdStudent")
    @CircuitBreaker(name = "fallback",fallbackMethod = "fallbackDebited")
    Debited getPayment(@RequestParam("idLecture") Long idLecture,
                       @RequestParam("idStudent") Long idStudent);
    default  Debited fallbackDebited(@RequestParam("idLecture") Long idLecture,
                                     @RequestParam("idStudent") Long idStudent,Throwable throwable){
        return  new Debited(1L,2L,new Date(),1000);
    }

    @PostMapping("/api/v1/transaction")
    @CircuitBreaker(name = "fallback", fallbackMethod = "fallbackJoinGroupe")
    ResponseEntity<?> joinGroupe(@RequestBody PayLecturesDto payLecturesDto);

    default ResponseEntity<?> fallbackJoinGroupe(PayLecturesDto payLecturesDto, Throwable throwable) {
        return ResponseEntity.status(503).body("Payment service is unavailable.");
    }


}
