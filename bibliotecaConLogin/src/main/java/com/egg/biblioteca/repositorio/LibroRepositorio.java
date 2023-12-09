/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.repositorio;

import com.egg.biblioteca.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

/**
 *
 * @author Soso
 */
@Repository
public interface LibroRepositorio extends JpaRepository<Libro,Long> {
    
  
    /*
    -Indicamos que es un repositorio con @Repository
    -Indicamos que es una interface
    -Indicamos que hereda de JpaRepository
    -Le indicamos a JpaRepository que la entidad que maneja es Libro,
    y que la llave privada es de tipo Long
    -Como es una interface, los métodos no tienen cuerpo ni retorno
    */
  
    
    
    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public Libro buscarPorTitulo(@Param("titulo") String titulo);
    
    /*vinculamos los parámetros de nuestro método con los campos que se
    correspondan con nuestra query:
    -@Params indica que lo que se ponga entre paréntesis de este, hace referencia al 
    tìtulo que es ATRIBUTO del libro, y el String titulo, hace referencia al parámetro que le paso 
    para que busque la query:
    @Params("titulo) -> l.titulo (atributo del libro)
    String titulo -> :titulo (titulo que le estoy pasando a la query
    */
    
    @Query("SELECT l FROM Libro l WHERE l.autor.nombre = :nombre")
    public List<Libro> buscarPorAutor (@Param("nombre") String nombre);
    
    
   
    
    
    
}
