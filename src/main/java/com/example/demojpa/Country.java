package com.example.demojpa;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@Entity()
@Table(name = "mw_ecom_country", uniqueConstraints = {@UniqueConstraint(name = "abbreviation", columnNames = "abbreviation")})
public class Country {


    @Id
    //@GeneratedValue
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "com.example.demojpa.CustomIdentifierGenerator")
    protected Long id;

    @NotNull
    @NotEmpty
    private String abbreviation;

    @NotNull
    private String name;


    public Country(Long id, String abbreviation, String name) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.name = name;
    }
}
