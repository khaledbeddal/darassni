package com.github.wallet.restwebservice.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Getter
@Setter
@Entity
@Table(name="wallet_transaction")
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @Column(name = "id")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /*@NotBlank
    @NotNull
    @Column(name = "global_id", unique = true)
    private String globalId;*/

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private Type type;

    @Column(name = "lecture_id")
    private Long lectureId;


    @Column(name = "teacher_id")
    private Long teacherId;


    @NotNull
    @Column(name = "amount")
    private double amount;



    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;



    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public Transaction(){ }

    public Transaction( /*String globalId,*/ Type type, double amount, Wallet wallet, Long lectureId, Long teacherId) {
        /*this.globalId = globalId;*/
        this.type = type;
        this.amount = amount;
        this.wallet = wallet;
        this.lectureId = lectureId;
        this.teacherId = teacherId;

        this.lastUpdated = new Date();
    }
}
