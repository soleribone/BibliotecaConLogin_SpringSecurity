/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.excepciones;

/**
 *
 * @author Soso
 */
public class MiException extends Exception{
    
    
    /*
    -Puede pasar que en el metodo crearLibro, tenga un id nulo o un titulo vacío
    -Antes de persistir tenemos que persistir que los datos están llegando
    -Queremos exception personalizadas*/
    
    /*este es el constructor de la clase MiException
    //este contructor recibe un mensaje, llama al padre y le pasa el mensaje
    //esta clase es para diferenciar los errores de la lógica del negocio, de los errores
    yt bugs propios del sistema*/
    public MiException(String msg){
        super(msg);
    
    }
    
}
