package com.github.wallet.restwebservice.service.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
@Setter
@Getter
@Builder
public class TransactionDTO {

    /*private String globalId;*/
    private int typeId;
    private double amount;
    private long walletId;
    private Long lectureId;
    private Long teacherId;
    private Date lastUpdated;


    public TransactionDTO(){ }

    public TransactionDTO(/*String globalId, */int typeId, double amount, long walletId, long lectureId, long teacherId, Date lastUpdated){
        /*this.globalId = globalId;*/

        this.typeId = typeId;
        this.amount = amount;
        this.walletId = walletId;
        this.lectureId = lectureId;
        this.teacherId = teacherId;
        this.lastUpdated = lastUpdated;

    }
}
