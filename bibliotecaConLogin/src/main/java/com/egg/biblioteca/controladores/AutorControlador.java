/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicio.AutorServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Soso
 */
@Controller
@RequestMapping("/autor") //localhost:8080/autor
/*si ingreso a esa url ingreso a este componente de tipo CONTROLADOR*/
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;
      /*VIDEO 10: creamos un formulario para mapearlo con un controlador, 
    será un formulario para cargar autores, cuando le ingresamos un autor al formulario
    el nombre del autor viaja hacia un controlador que lo resiva a través de la url,
    y de esa manera poder desarrollar con el las gestiones que consideremos necesarias:
    en este caso guardar el autor en la base de datos*/
    
    
    /*
    -Si lo que quiero es gestionar exclusivamente autores, creamos un controlador propio para
    esa clase
    -Dentro de este controlador creamos un mètodo de registro que es el que va a abrir
    la vista del formulario donde ingresamos al autor*/
    
    
 /*esta petición get va a responder al llamado de la url: //localhost:8080/autor/registrar */
    @GetMapping("/registrar")
    public String registrar() {

        return "autor_form.html";
    }

    /*
    -a través de un método post va a viajar la información del formulario de autor_form.html
    y esta información del método post va a tener un action en una url específica que es
    "/autor/registro", ese es el nombre de nuestro action.
    -Como ya estamos en "/autor", en el metodo solo ponemos "/registro"
    -Para indicarle al controlador que "nombre" es un parámetro que va a viajar en la url,
    lo marcamos como un @RequestParam, esto indica que es un parámetro requerido (osea que es
    obligatorio que llegue), y que va a llegar cuando se ejecute el formulario.
    -Este método lo que tiene que hacer es tomar el dato que está llegando y derivarlo al componente
    de servicios que es donde está el método que llamaba al repositorio para impactar esos cambios
    en la base de datos.
    -En el controlador creamos una instancia del método crearLibro del servicio para poder usarlo.
    */
   /*@PostMapping("/registro")
    public String registro(@RequestParam String nombre) { 
    
        
        try {
            
            autorServicio.crearAutor(nombre);
            
            return "redirect:/index.hmtl";
        } catch (MiException ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
              return "index.hmtl";
        }
        //llamamos a este método del servicio, y el autor debería persisitirse en la base de datos.
        //en esta última capa, manejamos las excepciones con un try-catch
        
    
      
    /*creamos un método que va a recibir el nombre que necesitamos,
        -->observemos que EL PARÁMETRO QUE LLEGA AL MÉTODO SE LLAMA DE LA MISMA MANERA QUE EL
        ATRIBUTO name DEL INPUT
     
        */
    //};
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo){
        
        try {
            autorServicio.crearAutor(nombre);
            
            modelo.put("exito", "El Autor fue registrado correctamente!");
        } catch (MiException ex) {
                      
            modelo.put("error", ex.getMessage());
            return "autor_form.html";
        }

        return "index.html";
    }

    //este método se creó en el video 13 para crear una tabla con Thymeleaf:
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Autor> autores = autorServicio.listarAutores();

        modelo.addAttribute("autores", autores);

        return "autor_list.html";
    }
    
    /*este métdod reconoce al autor a modificar por medio del id, y logramos que lo reconozca con el @PathVariable, una notación que indica 
    que este valor determinado va a viajar a través de un "path", es decir, a través de un fragmento de la url en el que se encuentra determinado
    recurso. Para esto ponemos "{id}" y con esto indicamos que a través de la url queremos enviar determinado recurso, en este caso el valor
    del id.
    -> Con @PathVariable le estamos diciendo a Spring que String id es un variable tipo "Path", y que va a viajar en la url "/modificar/{id}"
    ->Lo que queremos es que, cuando se renderize la vista del formulario a modificar, vengan los datos precargados de la id al que yo quiero 
    modificar, es decir, nos traiga el nombre actual del autor que vamos a modificar
    -->Para buscar el autor que tiene ese id, creamos un método en autorServicio que nos devuelva el llamado al método getOne del repositorio*/
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
    
        //inyectamos los datos de este autor a través del modelo: indicamos la key "autor", y el valor es el que devuelva el método del servicio que retorna el valor por id.
        modelo.put("autor",autorServicio.getOne(id));
        
        return "autor_modificar.html";
    }

    /*Este método recibe los datos del formulario para editar un autor con nuevos datos.
    --> recibe el dato del id como @PathVariable tmb, y el Sting del nombre nuevo*/
    @PostMapping("modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo){
    
        try {
            autorServicio.modificarAutor(id, nombre);
            
            /*si todo sale bien, quiero que se retorne la vista de la tabla con los cambios hechos, para esto usamos un "redirect" 
            hacia lista, que es la url que me muestra la tabla*/
            return  "redirect:../lista";
        } catch (MiException ex) {
            
            /*si algo sale mal, hacemos uso de nuestro modelo, y le ingresamos una key error, con el mensaje de error como valor.
            ->y también retornamos el formulario para modificar el autor.*/
           modelo.put("error", ex.getMessage());
           return "autor_modificar.html";
        }
    
    }
    
    
}
