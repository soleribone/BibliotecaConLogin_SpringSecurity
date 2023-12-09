/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.servicio;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorio.AutorRepositorio;
import com.egg.biblioteca.repositorio.EditorialRepositorio;
import com.egg.biblioteca.repositorio.LibroRepositorio;
import java.util.ArrayList;
import java.util.Date;
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
public class LibroServicio {
    
    /*Los métodos que se van a crear son los que requiere el cliente, y estos son:
    -Que se cree un libro
    -Que se puedan editar los datos del libro
    -Permitir que un libro se habilidato o deshabilitado
    */
    
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    /*
    -Para las funciones de persistencia generamos una instancia de la clase Libro Repositorio
    
    -INYECCIÓN DE DEPENDENCIAS: Con @Autowired le indicamos al servidor de aplicaciones 
    que esta variable repositorio va a ser inicializada por él, entonces no hace falta 
    que la inicializemos en el IDE. La anotación @Autowired en Java se utiliza en el contexto de Spring Framework 
    y se utiliza para realizar la inyección de dependencias automáticamente. 
    En otras palabras, @Autowired le indica a Spring que debe proporcionar el valor adecuado para un campo, 
    método o constructor marcado, resolviendo automáticamente las dependencias necesarias.
    
    -Hacemos lo mismo con autor y editorial repositorio para poder setearlos en los atributos del libro
    
    -TRANSACCIONES:estas se indican con @Transactional, y con esto especificamos que si el método
    se ejecuta sin excepciones, se realiza el commit a la base de datos y se aplican los cambios,
    caso contrario se hace un rollback.
    LOS MÈTODOS QUE GENEREN CAMBIOS EN LA BASE DE DATOS deben indicarse como @Transactional*/
    
    @Transactional
    public void crearLibro(Long isbn,String titulo,Integer ejemplares, String idAutor, String idEditorial) throws MiException{
        
        //pusimos las validaciones en un solo método que podemos replicar y no repetir código:
        //recordar poner "throws MiException" en el método.
        validar(isbn,titulo,ejemplares,idAutor,idEditorial);
        
        Autor autor = autorRepositorio.findById(idAutor).get();
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();
        /*buscamos al autor y a la editorial por id, y se la asignamos al nuevo objeto autor y editorial
        y el método get() nos devuelve el autor o editorial que encuentra por id.*/
        
        Libro libro = new Libro();
       
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        
        libro.setAlta(new Date()); //para que se setee con la fecha en la que se crea el objeto
        
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        
        libroRepositorio.save(libro);//llamamos al método "save" de JpaRepository para hacer la persistencia del objeto
    
          
    }
    
    //método para listar Libros
    public List<Libro> listarLibros(){
    
        List<Libro> libros = new ArrayList();
        
        libros = libroRepositorio.findAll();
        /*usamos el método findAll() que devuelve todos los registros de la tabla libro*/
        
        return libros;
    
    }
    
    
    //método para modificar un libro
    public void modificarLibro(Long isbn, String titulo, String idEditorial, String idAutor, Integer ejemplares) throws MiException{
    
         validar(isbn,titulo,ejemplares,idAutor,idEditorial);
    
    
    /*
    -Si el id que nos pasaron por parámetro es incorrecto, o el libro vinculado al id no existe usamos
    la clase "OPTIONAL" DE JPA. El optional es un obejto contenedor que puede o no obtener un valor
    no nulo:
    no nulo -> True, y nos retorna el valor con get()
    nulo -> False
    Luego, si la respuesta está presente, indicamos que se modifique el libro, caso contrario, no.
    -Con el título y con la editorial pasan lo mismo, puede haber un error en la id, o no existir
        el que buscamos con esa id, por lo que tmb usamos el optional*/
    
   
    
    Optional<Libro> respuesta = libroRepositorio.findById(isbn);
    Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idAutor);
    Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
    
    Autor autor = new Autor();
    Editorial editorial = new Editorial();
    
    if(respuestaEditorial.isPresent()){
    
        autor= respuestaAutor.get();
        
    }
    
    if(respuestaEditorial.isPresent()){
    
        editorial = respuestaEditorial.get();
    }
    
    
    if(respuesta.isPresent()){
        Libro libro = respuesta.get();
        
        libro.setTitulo(titulo);
        
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setEjemplares(ejemplares);
        
        
        libroRepositorio.save(libro);

    }
    
    }
    
    private void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException{
     
        if(isbn==null){
         throw new MiException("el isbn no puede ser nulo");
        }
        if(titulo.isEmpty() || titulo==null){
         throw new MiException("El titulo no puede ser nulo o vacío");
        }
           if(ejemplares==null){
         throw new MiException("los ejemplares no puede ser nulo");
        }
        if(idAutor.isEmpty() || idAutor==null){
         throw new MiException("El titulo no puede ser nulo o vacío");
        }
        
    }
    
}
