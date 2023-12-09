
package com.egg.biblioteca.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
CON LA CLASE ADMINISTRADOR podemos manejar todos los métodos que vayan a urls que solo van a poder administrar los admins
 */
@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    /*
    6.1_
    ->creamos un método que nos muestre un html que nos muestre todas las funcionalidades que tiene un administrador
    ->Cómo todos los métodos de esta clase requieren una preautorización, debemos configurar toda la clase para que esto ocurra,
    y lo hacemos desde nuestra clase de seguridad "SeguridadWeb"
    */
    
    @GetMapping("/dashboard")
    public String panelAdministrativo(){
    
        return "panel.html";
    
    }
    
}
