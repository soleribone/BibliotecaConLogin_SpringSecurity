/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.repositorio;

import com.egg.biblioteca.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Soso
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,String> {
    
    /*Recordemos que para que el método funcione tenemos que pasar los parámetros al método*/
    /*Este método busca un email según el parámetro que nos llega, y en base a eso nos retorna el
    usuario que tiene emparejado ese email.
    */
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
}
