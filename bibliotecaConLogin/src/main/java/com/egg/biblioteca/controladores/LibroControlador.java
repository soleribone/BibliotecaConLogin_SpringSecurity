/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicio.AutorServicio;
import com.egg.biblioteca.servicio.EditorialServicio;
import com.egg.biblioteca.servicio.LibroServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Soso
 */
@Controller
@RequestMapping("/libro")
public class LibroControlador {
    
    /*VIDEO 11: hoy vamos a aprender a usar Thymeleaf que es un motor de plantillas que nos
    permite añadir atributos dinámicos a etiquetas de html, vamos a poder inyectar, a través 
    de un parámetro llave-valor, tanto mensajes, como iterar listas, seleccionar determinados atributos
    de los objetos que queremos manejar, evaluar condiciones para mostrar una u otra cosa, etc
    
    @anotaciones(java)
    @decoradores(python)
    
    */
    @Autowired
    private LibroServicio libroServicio;
    
    @Autowired
    private AutorServicio autorServicio;
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
    
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        //una vez que tenemos las lístas, anclamos estas a un modelo para que sean enviadas a nuestra interfaz de usuario:
   
        modelo.addAttribute("autores",autores); //le pasamos al modelo, bajo la llave "autores" la lista autores.
        modelo.addAttribute("editoriales",editoriales);
        //con los datos cargados con esto, podemos trabajar con los datos que nos envía el modelo desde el html (vamos a libro_form.html)
        
    return "libro_form.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam(required=false) Long isbn, @RequestParam String titulo,
                         @RequestParam(required=false) Integer ejemplares, @RequestParam String idAutor, //ideAutor es lo que se escribe como el atributo "name" en la etiqueta "select" en libro_form.hmtl para poder llenar el combobox de la vista
                        @RequestParam String idEditorial, ModelMap modelo){
    
        
        /*cuando intentamos mandar a nuestro controlador un isbn nulo, no se ingresa al controlador
        y por lo tanto no se ejecuta la validación que ya nos avisaba que venía nulo, para esto le 
        adjuntamos a @RequestParam un required=false, de esta forma, si viene nulo, entonces va a 
        ingresar al controlador y manejaremos la excepcion desde el método del servicio*/
        
        
        try {
            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "El libro fue cargado correctamente");

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            //si da error enviamos el mensaje guardado en exceptions

            //y si hay error que se vuelvan a inyectar los autores y editoriales en los select correspondientes
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            return "libro_form.html";
        }

        return "index.html";

    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        List<Libro> libros = libroServicio.listarLibros();
        modelo.addAttribute("libros", libros);
        
        return "libro_list";
    }
    
    /*VIDEO 11: ModelMap
    THYMELEAF es un motor de plantillas que nos permite añadir atributos dinámicos a las etiquetas html,
    vamos a poder inyectar a través de un parámetro "llave-valor" tanto mensajes, como iterar listas, seleccionar
    determinados atributos de los objetos que queremos manejar, evaluar condiciones para mostrar una u otra cosa, etc.
    
    ->Para mostrar los mensajes de las excepciones a los usuarios por el front usamos model maps
    la clase model maps la vamos a crear como parámetro de nuestro método controlador
    ->Los modelos en spring sirven para que nosotros insertemos en el parametro modelo 
    toda la información que vamos a mostrar por pantalla, o que vamos necesitar utilizar
    en la interfaz del usuario
    ->El método "put" trabaja con el formato de llave-valor, en este caso, le asignamos a la llave
    de "exito" un mensaje de que funciona bien
    ->El html sabe que tiene que disparar determinado valor por pantalla si le colocamos el 
    model como atributo a la etiqueta "div" del mensaje de error y de éxito. Lo hacemos con 
    la tag de tymeleaf "th:if="${error!=null}" lo mostramos, osea, si el error no es nulo lo mostramos
    y lo mismo con el mensaje de exito
    ->El simbolo "$" hace referencia a una variable que va a viajar.
    ->Luego en la etiqueta de párrafo indicamos con el atributo "th:text="${error}"  que el mensaje
    de error o de éxito se muestre en el párrafo, porque devolvería el mensaje asociado a la llave "error"
    de la función model.put
    ->Recordemos "avisarle" a la plantilla de html que estamos usando thymeleaf poniendo encima
    del arrchivo la ruta hacia la pagina de thymeleaf.
    */
    
    
     /*VIDEO 12: SELECT
    Thymeleaf tiene muchas tags que nos ayudan a la hora de indemnizar los html.
    
    ->en la vista observamos que tenemos dos combobox en donde se deben cargar las opciones de libros y autores,
    estos es asì porque en el html del formulario tenemos elementos con etiquetas "select" para poder seleccionar un
    autor y una editorial para vincularlas a un libro cuando lo creemos.
    ->Con las etiquetas select agregadas al formulario, necesitamos que nos muestre un listado de los a
    autores y editoriales que tenemos cargados en la base de datos, para lograr esto en el controlador
    vamos a llamar al objeto modelo de tipo "ModelMap" para que nos envíe la información correspondiente a través de un formato
    llave-valor, para eso en autorServicio
    
    -> lo que querémos es que, cuando se renderize el libro_form.html mediante el metodo "registrar", nos traiga los datos de autores
    y editoriales dentro de los sets, para eso le pasamos ModelMap por parámetros a la funcion tipo get "registar"
    
    ->Para lograr esto, dentro de nuestro getMapping (metodo "registrar") vamos a inyectar los valores de autor y editorial 
    que tenemos previamente cargado, y para eso le pasamos como argumento a la función el objeto modelo de tipo "ModelMap" 
    para que a través de pueda indectar todo lo que necesite
    
    ->Como necesitamos que nos traiga TODOS los autores o editoriales que hay en la base de datos, hacemos uso de los servicios 
    (autorServicio, editorialServicio) en donde están los metodos "listarAutores" y "listarEditoriales"
    */
    
    
    /*VIDEO 13: TABLAS
    
    ->Ya tenemos la lógica necesaria para crear obejtos de cualquiera de la entidades, ahora vamos a aprender a listarlas en una tabla
    usando Thymeleaf.
    -> Las modificaciones de este video se hicieron en autorControlador y en libroControlador, en donde agregamos los métodos "listar" de tipo GetMapping
    ->También se agregaron los templates "libro_list.html" y "autor_list.html" en donde se muestran las tablas en la vista de la página
    */
    
    /*VIDEO 14: MODIFICAR
    ->Creamos los métodos que permiten modificar los datos de nuestras entidades: en la vista de la página se agregó una columna llamada "acciones"
    en donde está la opción de "modificar", que nos abre la vista de un formulario para modificar ingresando los datos por este formulario, así modificamos
    los datos de esa fila.
    ->Para esto agregamos en el template "autor_list.html" la columna "acciones" en donde está el link para ingresar el formulario en donde editamos los datos
    -->En autorControlador creamos el método "modificar", al que se le agrega el argumento "PathVariable" con el dato del id del autor que queremos
    modificar.
    -->También agregamos un método en autorServicio que nos traiga un autor por ID
    -->También agregamos un nuevo template: "autor_modificar.html" que es la vista en donde modificamos el autor.
    -->En autor_modificar.html hay una explicación sobre las variable de pre-procesamiento y como ocultar el id para que no se pueda modificar
    -->En autorControlador también escribimos un método @PostMapping que recibe los datos del formulario con los datos nuevos para modificar los viejos
    -->finalmente modificamos autor_list.html para que el link "modificar" funcione, agregandole la variable de pre procesamiento a la url de la etiqueta "a", quedando:
    <a th:ref = "@{/autor/modificar/__${autor.id}__">  modificar </a>, con lo que se activa el link "modificar" en cada fila de la tabla
    */
}

