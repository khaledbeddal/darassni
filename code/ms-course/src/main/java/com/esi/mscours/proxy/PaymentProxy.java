package com.esi.mscours.proxy;

import com.esi.mscours.model.Debited;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

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


}
