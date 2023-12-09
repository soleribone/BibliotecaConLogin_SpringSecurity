/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Soso
 */
@Entity
public class Editorial {
    
    @Id
    @GeneratedValue(generator="uuid") 
    /*crea un valor auto generado, el valor del id se va a generar de forma automática, 
    al momento en que el repositorio persista la entidad
    */
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    /*indicamos como estrategia alternativa al generador uuid tenemos otra estrategia,
    de esta manera nos aseguramos de que ninguna id se repita*/
    private String id;
    private String nombre;

    public Editorial() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
