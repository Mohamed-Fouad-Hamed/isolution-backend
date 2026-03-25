package com.alf.accounts_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="id_sequence")
public class IdSequence {
    @Id
    private String entityName;
    private String codeLen ;
    private long lastNumber;
}
