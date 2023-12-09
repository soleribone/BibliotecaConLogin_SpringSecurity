/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Soso
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Imagen {
    
     /*VIDEO 9: Imagen: Entidad - Repositorio - Servicio
    ->En los próximos videos vamos a aprender a cargar imágenes como foto de perfil de usuario.
    ->Primero creamos la entidad "Imagen"
    ->Luego en la entidad Usuario hacemos la relación: un usuario va a tener una imagen
    ->Luego creamos el repositorio de la imagen: ImagenRepositorio
    ->Luego creamos el servicio: imagenServicio (explicación continúa en 9.1_ el servicio)
    */
    
    /*VIDEO 10: Vinculación con Servicio Usuario
    -> La explicación está en imagenServicio*/
    @Id
    @GeneratedValue(generator="uuid") 
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String mime; //este atributo almacena el formato del archivo de la imagen
    private String nombre;
    
    @Lob @Basic(fetch= FetchType.LAZY)
    private byte[] contenido; //con la notación lob le decimos a spring que este dato puede manejar muchos bytes
                              //con Basic definimos que el tipo de carga va a ser "lenta" o "perezosa" con FETCH.
                              /*todo esto va a querer decir que el atributo contenido se va a cargar SOLAMENTE
                                cuando lo pidamos, haciendo las querys más livianas, osea, cuando querramos acceder 
                                a la imagen no nos va a traer todo el contenido, sino los que no tiene la indicación fetch,
                                y este atributo se carga si le hacemos un get.*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    
    
}
