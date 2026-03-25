package com.alf.auth.models;


import com.alf.core_common.enums.AuthorityName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTHORITY")
public class Authority {

    @Column(name = "name",nullable = false, length = 50)
    @Id
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @JsonIgnore
    @ManyToMany(mappedBy = "authorityNames")
    private Set<User> users = new HashSet<>();

}
