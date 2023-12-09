/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca;

import com.egg.biblioteca.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 *
 * @author Soso
 */
@Configuration
@EnableWebSecurity                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter{
    
    @Autowired 
    public UsuarioServicio usuarioServicio;
    
    /* 4.1_
    ->con el objeto Authentication que recibimos por parámetro, configuramos el manejador de seguridad que tiene springsecurity, y al que le vamos
    a decir cual es el servicio que tiene que utilizar para AUTENTICAR UN USUARIO
    ->Al método userDetailService que hicimos en videos anteriores le pasamos por parámetro el servicio que tiene que usar.
    ->Entonces para AUTENTICAR a los usuarios que ingresan a nuestra aplicación, vamos a usar UsuarioServicio, por eso se lo pasamos por parámetro.
    ->Finalmente codificamos la contraseña con passwordEncoder, al que le pasamos un nuevo objeto "BcryptPasswordEncoder".
    ->Entonces, cuando se REGISTRA un nuevo usuario, los AUTENTICAMOS con userDetailService, y luego lo CODIFICAMOS con passwordEncoder.
    ->También codificamos la contraseña en el método registrar en UsuarioServicio (continúa en 4.2_)
    */
    @Autowired 
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
    
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
        
       
    }
    
    /*
    VIDEO 1: Dependecias y configuraciones iniciales
    ->para que esta clase funcione tiene que tener las anotaciones configuration, enabledwebsecurity y la tercera declarada antes de la clase
    ->también debemos heredar WebSecurityConfigurerAdapter*/
    
    /*Vamos a crear el método protegido: configure(), que se sobreescribe porque ya está disponible,
    ->este método recibe por parametro un objeto HttpSecurity
    ->El objeto http que ingresa por parámetro va a autorizar determinados parámetros, a esto lo indicamos con authorizenHttpRequest().
    ->Con antMatcher y permitAll(), le decimos que cuando ingresemos a determinadas partes de nuestro sistema, permita los archivos
    cuyas rutas tengans css, javascript, imagenes y otros archivos estáticos a los que querramos acceder,
    esto va a hacer que los css, imagenes, etc, van a ser permitidos para cualquier persona que acceda
    no hace falta ser un usuario regostrado para poder ver este tipo de archivos.
    ->Después de indicar este método, si vamos a la página(localhost:8080) veremos que ya no me obliga a loguearme, y que me
    muestra el index como estaba antes.
    */
    
   
    @Override
    protected void configure(HttpSecurity http) throws Exception{
    
 
        /*5.1_
        ->Ahora agregamos la configuración para loguearse*/
        http
                .authorizeRequests()
                    .antMatchers("/admin/*").hasRole("ADMIN") //con esto indicamos que a la url /admin/loquesea se puede acceder con el rol de administrador
                    .antMatchers("/css/*","/js/*","/img/*","/**")        /*en esta línea establecimos que los recursos que estuviesen dentro de estas carpetas fueran accesible para cualquier usuario
                                                                            sin necesidad de estar logueado y sin tener permisos*/
                    .permitAll()
                .and().formLogin() //configuramos el login
                            .loginPage("/login") //decimos que nuestra url donde se va aencontrar el formulario de login que queremos mostrar
                            .loginProcessingUrl("/logincheck") //configuramos cual es la url con la que springsecurity va a autenticar un usuario
                            .usernameParameter("email") //configuramos la credencial del nombre de usuario (que es el email)
                            .passwordParameter("password") //configuramos la credencial de la contraseña
                            .defaultSuccessUrl("/inicio") //si el login es correcto, por defecto queremos que nos mande al url del inicio
                            .permitAll()
                .and().logout() //también configuramos la salida, osea el logout
                            .logoutUrl("logout") //cuando un usuario ingrese a determinada url, que se cierre la sesión
                            .logoutSuccessUrl("/")  //indicamos que cuandos se cierre la sesión se vuelva al index            
                            .permitAll()
                .and().csrf()
                            .disable(); //aclaramos que desabilite algo que trae stringsecurity de forma instriseca.
                
                }
    
    /*VIDEO 2: Entidad Usuario + Enum Roles
    ->Creamos una entidad usuario con sus atributos
    ->Cada usuario va a tener un rol determinado, por eso creamos un paquete que va a tener las enumeraciones
    por eso creamos desde nuestro paquete raíz un nuevo paquete llamado "enumeraciones", y dentro de 
    este paquete creamos un enum llamado "Rol" con los roles usuario y administrador.
    ->Luego agregamos estos roles como atributo en la entidad usuario(avisamos con la anotación @Enumerated que es una enumeracion.
    ->Luego creamos el repositorio de la clase Usuario creando una nueva clase UsuarioRepositorio en el package repositorio.
    ->En la clase UsuarioRepositorio creamos un método que retorne un usuario según su email.
    ->Luego creamos un controlador que responda a las siguientes peticiones: que un usuario ingrese con su email y contraseña.
    ->Con estas ultimas dos cosas podemos modelar usuarios y tenemos los accesos a ellos y su seguridad.
    */
        
    /*VIDEO 3: Servicio Usuario - Permisos
    ->Creamos la clase UsuarioServicio y agregamos los métodos registrar y validar
    ->Como necesitamos autenticar a cada usuario que se loguee en la aplicación, hacemos que la clase usuarioServicio
    implemente una interfaz userDetailsService, lo que nos hace implementar los metodos abstractos de la interfaz, 
    y esto nos crea un método con un override llamado "loadUserByUserName" (en UsuarioServicio continúa la explciación
    */
    
    
    /*VIDEO 4: Registro Usuario
    ->En este video generamos la lógica necesaria para que un usuario se pueda registrado y que sus datos sean persistidos en la DB.
    ->En registro.html vemos que están los inputs para cada dato que hay que ingresar, y observamos que los nombres con los que viajan los valores
    que ingresamos son los mismos que los nombre que esperan nuestros métodos del servicio y del controlador.
    ->Explica cosas de thymeleaf, está todo escrito en registro.html
    ->En el controller PortalControlador escribimos el método "registro()" que va a recibir los valores del formulario
    ->Luego encriptamos la contraseña en nuestra DB con la clase "BCryptPasswordEncoder", en este paso escribimos codigo en SeguridadWeb.
    ->Creamos una instancia de usuarioServicio, y luego creamos un método que lleva autowired llamado "configureGlobal" que recibe como paámetro un
    objeto de tipo Authentication (explicación continúa en 4.1
    */
    
    /*VIDEO 5: Login + antMatchers
    ->En este video le damos la funcionalidad para que puedan loguearse, para esto trabajamos sobre la clase SeguridadWeb sobre el método "configure".
    ->La explicación continúa en 5.1_ el método "configure()"
    ->Luego vamos al template login.html para comprobar que estén los nombre iguales que en la configuración de configure()
    ->Luego creamos un método getmapping "login()" en PortalControlador 
    */

    /*VIDEO 6: Agregar seguridad a los accesos a la aplicación
    -> En el template inicio.html agregamos una etiqueta más con el botón cerrar sesión para el logout con un href "/logout"
    -> En el metodo getMapping inicio() en PortalControlador, hacemos las modificaciones para que no permita ingresar al inicio() de un usuario logueado
    sin estar logueado, le decimos que necesitamos una preautorización para poder ingresar al controlador inicio() que me muestra el inicio de un
    usuario logueado, y hacemos esto con la anotación "@PreAuthorize"
    ->Luego también creamos un controlador nuevo llamado "AdminControlador" con el que podeamos registrar un usuario con el rol administrador, es decir, un usuario que pueda modificar los autores
    libros o editoriales, y no solo verlos como los usuarios comunes.
    ->Sigue en 6.1_ en AdminController
    ->Para lograr que todos los métodos de la clase AdminControlador tengan preautorización, modificacamos el método configure() en la clase
    SeguridadWeb para lograr eso agregando un antMatcher y asignandole el rol administardor.
    ->ENTONCES, podemos lograr preautorizar un método con la anotación "@Preauthorize sobre el método, o toda la clase agregando el antMatchers con su respectivo rol en
    el método configure().
    ->Luego creamos un usuario con permisos de administrador, y este va a poder editar otros usuarios y darles a sus vez el rol de administador, para eso seguimos los siguientes pasos:
                a)registramos un nuevo usuario desde el frontend. 
                b)En workbench cambiamos el rol de usuario de USER a ADMIN manualmente.
                c)Ingresamos con ese usuario a la página y vemos si podemos ingresar a dashboard
                d)Ahora queremos que ingrese directamente al dashboard, eso lo vemos en el otro video.
    
    */
    
    /*VIDEO 7: Recuperar información de la sesión - HttpSession
    ->Vamos a usar la libreria springsecurity para recuperar la información sobre el usuario que está logueado, y poder utilizar dicha información en 
    las vistas, asì lograremos que cuando se loguee un usuario tipo administrador, me muestre la vistab del dashboar y no el inicio común de todos los usuarios.
    ->Primero agregamos funciones a nuestro método "loadUserByUserName()" de UsuarioServicio (sigue en 7.1_)
    ->Finalmente, en PortalControlador, modificamos el método inicio() (sigue en 7.2_ en PortalControlador).
    
    */
    
    /*VIDEO 8: Thymeleaf: secAuthorize y :fragments:
    -> En todas las vistas repetimos la etiqueta "head", y casi la misma barra de navegación. Este código se puede simplificar con la funcionalidad 
    "fragments" de thymeleaf. Lo haremos es crear una plantilla que tenga una porción de html que se reutilice en varias vistas.
    ->Creamos un packete en templates que se llama "templates.fragments" y dentro creo el html "head.html"
    ->En el "head.html" guardamos la etiqueta head que reutilizamos en todas ls vistas.
    ->Luego en todos los templates con el mismo head usamos la etiqueta de thymeleaf "th:replace = "referenciaalpaquete" " para que se use este head genérico.
    ->En index.html se hizo uno de los reemplazos del head.
    ->Hicimos lo mismo con navbar.
    
    
    ->Según los permisos de los usuarios se van mostrando diferentes funcionalidades en la barra de navegación, por ejemplo, si como usuario común puedo acceder
    a la lista de los libros, y en esa lista tengo la acción de modificar el registro, vamos a hacer las modificaciones para que solo los usuarios admin les 
    aparezca esta acción.
    ->Instalamos la dependencia de seguridad de thymeleaf.
    ->Modificamos la vista libro_list.html
    ->También podemos condicionar que una etiqueta del html se vea o no según si el usuario está logueado o no.
    
    ->Los links de cerrar sesión, listar, editar, deben verse si el usuario inició sesión.
    ->Las condiciones se tiene que agregar en todos los html
    
    ->Finalmente en inicio.html, mostramos el nombre del usuario logueado haciendo un llamado a un atributo
    */
 
}

