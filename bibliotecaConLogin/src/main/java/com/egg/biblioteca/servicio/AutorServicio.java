/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.servicio;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorio.AutorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Soso
 */
@Service
public class AutorServicio {
    
    @Autowired
    AutorRepositorio autorRepositorio;
    
    @Transactional
    public void crearAutor(String nombre) throws MiException{  //recibe solo el nombre porque el id se genera automaticamente
    
        validar(nombre);
       Autor autor = new Autor();
       
       autor.setNombre(nombre);
       
       autorRepositorio.save(autor);
        
    } 
    
    
    public List<Autor> listarAutores(){
        
    List<Autor> autores = new ArrayList();    
    
     autores = autorRepositorio.findAll();
     
     return autores;
    
    } 
    
    
    public void modificarAutor(String id, String nombre) throws MiException{
        
        validar(nombre);
    
    Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Autor autor = respuesta.get();

            autor.setNombre(nombre);

            autorRepositorio.save(autor);

        }

    }

    //este método nos retorna el autor que tiene el id que le pasamos
    public Autor getOne(String id) {

        return autorRepositorio.getOne(id);
    }
    

    private void validar(String nombre) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el titulo es nulo o esta vacío");

        }

    }


   
    
    
}
