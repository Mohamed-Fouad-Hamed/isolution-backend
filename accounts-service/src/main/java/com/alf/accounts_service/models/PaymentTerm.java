package com.alf.accounts_service.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_term")
public class PaymentTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(
            mappedBy = "paymentTerm",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    @OrderBy("sequence ASC")
    private List<PaymentTermLine> lines = new ArrayList<>();

    public void addLine(PaymentTermLine line){
        line.setPaymentTerm(this);
        this.lines.add(line);
    }

    public void removeLine(PaymentTermLine line){
        this.lines.remove(line);
        line.setPaymentTerm(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<PaymentTermLine> getLines() {
        return lines;
    }

    public void setLines(List<PaymentTermLine> lines) {
        this.lines = lines;
    }
}

