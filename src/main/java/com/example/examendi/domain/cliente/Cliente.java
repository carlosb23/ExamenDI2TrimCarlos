package com.example.examendi.domain.cliente;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name="informacioncliente")
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre")
    private String nombre;

    @Column(name= "sexo")
    private String sexo;

    @Column(name = "peso")
    private double peso;

    @Column(name = "edad")
    private int edad;

    @Column(name = "talla")
    private double talla;

    @Column(name = "tipoActividad")
    private String tipoactividad;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "GER")
    private double GER;

    @Column(name = "GET")
    private double GET;
}
