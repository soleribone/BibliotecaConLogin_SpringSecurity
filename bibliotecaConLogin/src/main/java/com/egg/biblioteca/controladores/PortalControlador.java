/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicio.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Soso
 */
@Controller
@RequestMapping("/") /*esta configura cual es la url que va a escuchar a este controlador
en este caso está indicando que este controlador va a escuchar a partir de la barra "/" 
entonces cada vez que pongamos en nuestra url "localhost" y una barra
este controlador se va a activar, por ejemplo: localhost:8080/index */
public class PortalControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    /*Los controladores son los componentes que van a conectar:
    Interfaces de usuario html <----> capas de servicio con la lógica del negocio*/

    /*El primer método que se va a ejecutar es el index:
    @GetMapping va a mapear la url también cuando se le ingrese la barra "/"
    osea, cuando se ingrese la url (por ejemplo: localhost:8080/index) 
    se ejecute todo el cuerpo del método "index()"  */
    
    @GetMapping("/")
    public String index(){
    
     return "index.html";
        
     /*este método retorna la vista que se renderiza una vez que se entra a la barra*/
             
    }
    
    /*Sprin mapea las vistas por medio de su carpeta "templates", que está en othersources/resources */
    /*Parados sobre el proyecto, abrimos el navigator, y ejecutamos "springboot run
    luego si ponemos localhost8080/ en el navegador, me lleva al index.html que hice
    */
    
  
    /*VIDEO 2 SPRING 2: 
    ->Necesitamos un controlador con el que podamos ingresar con email y contraseña, por eso creamos
    un método getmapping que me renderize la vista del registro
    -> Hacemos lo mismo para el login
    ->Este GETMAPPING nos MUESTRA la página de registro
    */
    @GetMapping("/registrar")
    public String registrar(){
    
        return "registro.html";
    
    }

    /*
    @GetMapping("/login")
    public String login() {

        return "login.html";

    }  */
    
    
    /*VIDEO 3: creamos el método registro que va a recibir los valores del formulario
    ->Este método POSTMAPPING recibe los datos que se ingresan en el formulario
    ->Con el método "registrar" de usuarioServicio podemos realizar la acción de registrar el usuario,
    y si no que devuelva una excepción.
    ->Recordemos que ModelMap nos permite poner mensajes en nuestro html, en este caso, mensaje de exito o error según se pueda registrar o no el usuario.
    */
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, 
                            @RequestParam String password, @RequestParam String password2, ModelMap model, MultipartFile archivo){
    
        try {
           
            usuarioServicio.registrar(archivo,nombre, email, password, password2);
            model.put("exito", "Usuario registrado con éxito");
            model.put("nombre", nombre);
            model.put("email",email);
            
            return "index.html";
        } catch (MiException ex) {
            model.put("error", ex.getMessage());
            
            return "registro.hmtl";
        }
    
    }
    
    /*VIDEO 5: creamos un getmapping login() que sea el controlador del login que puede o no recibir un error, porque puede darse el caso que ingresemos
    mal las credenciales
    ->Como PUEDE o NO venir un error, el @RequestParam no está siempre requerido y lo aclaramos entre paréntesis
    ->Generamos también el getmapping para que se renderize la página inicio.html cuando el usuario se registre correctamente (como lo indicamos en el configure())*/
    @GetMapping("/login")
    public String login(@RequestParam(required=false) String error, ModelMap modelo){
    
        if(error!=null){
            modelo.put("error","Usuario o clave inválidos");
        }
        
        return "login.html";
    
    }
    
    /*VIDEO 6: con preauthorize le decimos a springsecurity que autorice el acceso al  método login bajo determiandas condiciones,
    especificamente le indicamos los roles que tienen acceso al inicio, osea, los que estén registrados.
    */
    /*VIDEO 7: modificamos el método para que nos muestre el dashboard cuando iniciemos sesión como administrador
    ->El metodo recibe un objeto HttpSEssion*/
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session){
        
        /*creamos un usuario que va a contener todos los datos de la sesión:*/
        Usuario logueado = (Usuario) session.getAttribute("usuariosession"); //usuariosession es el nombre que le seteamos a la sesion en el metodo "loadUserByName"
        
        /*luego validamos que el rol que tiene este usuario logueado es el de administrador, y si es así, redireccionamos a la vista dashboard
        de lo contrario retornamos la vista del inicio.html*/
        
        if(logueado.getRol().toString().equals("ADMIN")){
         return "redirect:/admin/dashboard";
        }
       
        return "inicio.html";
    }
    
    /*
    ->PreAuthorize nos dice que nos traiga la vista del perfil solo si el usuario está registrado con alguno de los
    roles, y logueado en la aplicación
    ->Este getMapping lo que hace es recuperar los datos del usuario a través del objeto tipo HttpSession llamado session
    
    */
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
    
        /*creo un usuario, y le guardo todos los datos del usuario que está logueado en la sesión, 
        y luego con el modelo pongo los datos para que se autocomplete el formulario de modificar
        con los datos que ya tiene el usuario, por si hay alguno que no quiera cambiar*/
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "usuario_modificar.html";
    
    }
    
    /*
    ->Acá si necesito pasar el id por la ruta, en el getmapping 
    no lo necesito porque tenemos acceso a traves de la HttpSession, y acá lo tenemos por medio del formulario
    ->PORQUÉ SE ACTUALIZA EL ID??
    */
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/perfil/{id}")
    public String actualizar(@PathVariable String id,@RequestParam String nombre, @RequestParam String email, 
                            @RequestParam String password, @RequestParam String password2, 
                            ModelMap model, MultipartFile archivo){
    
          try {
           
            usuarioServicio.actualizar(archivo,id,nombre, email, password, password2);
            model.put("exito", "Usuario actualizado con éxito");
         
            
            return "inicio.html";
        } catch (MiException ex) {
            model.put("error", ex.getMessage());
            model.put("nombre", nombre);
            model.put("email",email);
            
            return "usuario_modificar.hmtl";
        }
        
        
    
    }
}
