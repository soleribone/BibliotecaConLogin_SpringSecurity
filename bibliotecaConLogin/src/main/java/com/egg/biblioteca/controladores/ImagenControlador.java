/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Soso
 */
@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    /*VIDEO 11: 
    ->El siguiente método nos devuelve la imagen que está vinculada a un perfil de un usuario.
    ->Nos retorna un responseEntity que va a tener el contenido de la imagen, la respuesta que va a traer es el arreglo de bytes.
    ->creamos un método que devuelve un responseEntity que contiene un arreglo de bytes
    -> Este metodo recibe el id del usuario al que está vinculada la imágen por una url, por eso
    usamos @PathVariable
    ->Recordemos que el "{id}" se pone de esta manera porque es la forma en la que el @PathVariable puede
    recibir el id por parámetro
    */
    @GetMapping("(/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id){
    
        //buscamos al usuario por id
        Usuario usuario = usuarioServicio.getOne(id);
        
        /*
        ->traemos la imagen, y de la imagen traemos el contenido,
        porque eso es lo que necesitamos que el navegador descargue,
        las imágenes en html se consumen como una url, el navegador interpreta el arreglo de bytes
        y lo coloca donde tiene que estar con el tamaño que tiene que tener,etc
        ->luego guardamos el contenido en bytes de la imagen en la variable imagen*/
        byte[] imagen = usuario.getImagen().getContenido(); 
        
        /*
        ->Llamamos las cabeceras del pedido
        ->Las cabeceras le dicen al navegador que lo que estamos devolviendoe s una imagen, por eso tenemos que 
        setearle a headers el tipo de contenidoque va a llevar
        */
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        
       
        /*La función retorna un ResponseEntity que tiene 3 partes:
        1)La imagen como arreglo de bytes
        2)Las cabeceras
        3)El estado HTTP, que es el estado en el que termina el proceso, si fue exitoso señalamos un 200.
        Osea, estamos retornando una responseEntity de bytes que es la imagen del usuario en lugar de una vista html.
        */
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
           
    
    }
}
