/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.servicio;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorio.ImagenRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Soso
 */
@Service
public class ImagenServicio {

    /*(VIDEO 9) 9.1_
    ->Creamos un método para guardar una imagen
    ->El tipo de dato multipart que recibimos es el tipo de dato en el que se va a almacenar la imagen
    ->Creamos un método para actualizar la imagen, que recibe el archivo multipart que tiene la información nueva con la que 
    vamos a actualizar la imagen y el id de la imagen que queremos actualizar 
     */
    @Autowired
    private ImagenRepositorio imagenRepositorio;

    public Imagen guardar(MultipartFile archivo) throws MiException {

        /*validamos que el archivo que nos llega es distinto de nulo, luego si el archivo que nos llega por parametro es distinto de nulo
        recién entonces ejecutamos la lógica*/
        if (archivo != null) {

            try {
                Imagen imagen = new Imagen(); //inicializamos un objeto imagen y luego le seteamos datos

                imagen.setMime(archivo.getContentType()); //seteamos el mime con el contentType del archivo que es un atributo de multiPart

                imagen.setNombre(archivo.getName());

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen); //persistimos la imagen en la DB.

            } catch (Exception e) {

                /*si hay un error en el guardado se lanza una excepción*/
                System.err.println(e.getMessage());

            }

        }

        return null; //si el archivo que entra en el método es nulo, se retorna una entidad vacía o nula
    }

    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException {

        if (archivo != null) {

            try {
                Imagen imagen = new Imagen();

                /*validamos si el id que me llega por parámetro es nulo, osea validar si existe
                si existe, usamos un optional que nos va a traer la imagen del repositorio, porque SI EXISTE*/
                if (idImagen != null) {

                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);

                    /*si el objeto del repositorio que se guarda en respuesta EXISTE, esto hará que el optional sea true
                    en ese caso, guardaremos los datos del repositorio que están en respuesta en el objeto imagen
                    para luego poder setearle los NUEVOS datos, osea, para pisarle los datos viejos con los nuevos*/
                    if (respuesta.isPresent()) {

                        imagen = respuesta.get();

                    }

                }

                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getName());

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen); //persistimos los nuevos datos en el repositorio.

            } catch (Exception e) {

                /*si hay un error en el guardado se lanza una excepción*/
                System.err.println(e.getMessage());

            }

        }

        return null; //si el archivo que entra en el método es nulo, se retorna una entidad vacía o nula

    }
    
    
}
