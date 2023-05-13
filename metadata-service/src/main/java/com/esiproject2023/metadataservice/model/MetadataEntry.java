package com.esiproject2023.metadataservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "metadata")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
//@TypeDef(name = "json", typeClass = JsonStringType.class)
public class MetadataEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, name = "request", nullable = false)
    private String request;


//    @Type(type = "json")
    @Column(name = "response", nullable = false, columnDefinition = "TEXT") //    @Column(name = "response", nullable = false) //
    private String response;
}
