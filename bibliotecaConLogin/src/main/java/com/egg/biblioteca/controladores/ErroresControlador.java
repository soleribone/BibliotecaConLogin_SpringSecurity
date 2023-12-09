/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controladores;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Soso
 */
@Controller
public class ErroresControlador implements ErrorController {
    
    /*VIDEO 11:
    ->Observemos que el RequestMapping está a nivel del método y no a nivel de la clase.
    -> Con esto le estamos dando la orden a este método que entre todo recurso que venga con "/error", sea del metodo get o post, que
    todo recurso de estos tipo ingrese al método
    ->Recuperamos el código de error que viene del servidor, y en base a eso establecemos un mensaje particular para ese codigo de1 error,
    y redireccionamos o abrimos una vista específica
    */
    @RequestMapping(value="/error", method= {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest){
    
        /*el método recibe una petición http, en base a eso creamos una pàgina de error del tipo "ModelAndView" que le llamamos error porque
        trae la vista error.html*/
        ModelAndView  errorPage = new ModelAndView("error");
        
        
        /*creamos un string vacío para darle un valor dependiendo del case*/
        String errorMsg = "";
        
        /*creamos el entero que le ingresamos a cada case como valor del mensaje de error*/
        int httpErrorCode = getErrorCode(httpRequest);
        
        switch(httpErrorCode){
            case 400:
                errorMsg = "El recurso solicitado no existe";
                break;
        }    
             case 401:
                errorMsg = "El recurso solicitado no existe";
                break;
        }        
             case 402:
                errorMsg = "El recurso solicitado no existe";
                break;
        }        
              case 403:
                errorMsg = "El recurso solicitado no existe";
                break;   
        }        
                
        
        
        }
    
    }
    
    
}
