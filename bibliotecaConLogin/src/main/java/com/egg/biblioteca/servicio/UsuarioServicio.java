/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.servicio;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorio.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Soso
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    //pasamos dos contraseñas para validar que se ingrese correctamente
    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2) throws MiException {

        validar(nombre, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);

        /*4.2_
    antes de setear la contraseña la codificamos*/
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));

        /*como queremos que cuando un usuario se registre tenga rol de usuario y no de administraciòn
    se le asigna el ROL USER*/
        usuario.setRol(Rol.USER);

        /*igualamos la instancia de una imagen al llamado de la función guardar del servicio, 
        para guardar los datos del archivo multipart que se ingresa por parámetro en una imagen*/
        Imagen imagen = imagenServicio.guardar(archivo); 
        
        usuario.setImagen(imagen);
        
        usuarioRepositorio.save(usuario);

    }
    
    
    public void actualizar(MultipartFile archivo,String idUsuario, String nombre, String email, String password, String password2) throws MiException{
    
        validar(nombre, email, password,password2);
        
        /*buscamos al usuario por id, y verificamos con el optional que la respuesta esté presente
        a) Si la respuesta está presente le seteamos los atributos nuevos
        b) Si no está presente no hacemos nada
        */
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario); 
        
        if(respuesta.isPresent()){
        
            Usuario usuario = respuesta.get();
            
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setRol(Rol.USER);
            
            String idImagen = null;
            
            /*verificamos si el usuario tiene una imagen o no, si no es nula, traemos el id de la imagen de ese usuario ya cargado*/
            if(usuario.getImagen()!=null){
            
                idImagen = usuario.getImagen().getId();
            
            }
            
            /*luego creamos una instancia de imagen, a la que igualamos al método del servicio actualizar,
            que actualiza los datos con los datos del archivo multipart
            ->Como este método además de actualizar los datos, devuelve la imágen nueva, lo usamos para guardar la imagen en la variable
            imagen a la usaremos para setear el atributo imagen del usuario*/
            
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            
            usuario.setImagen(imagen);
            
            usuarioRepositorio.save(usuario);
        
        }
    
    
    }

    private void validar(String nombre, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede ser nulo o vacío");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("la contraseña no puede no puede ser nula. y debe tener más de 5 digitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas deben ser iguales");
        }

    }

    /*el siguiente método aparece cuando aceptamos los metodos abstractos del UserDetailService, 
    este metodo recibe un USERNAME por parámetro COMO STRING, para que nosotros podamos AUTENTICARLO
    ->Cambiamos userName por email, porque nuestro nombre de usuario va a ser el email, este va a ser
    el valor con el que vamos a autenticar a cada uno de los usuarios a través de su email y su contraseña.
    ->Primero buscamos un usuario de nuestro dominio y transformarlo en un usuario del dominio de SpringSecurity.
    
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        /*creamos una variable usuario en donde guardamos al usuario que buscamos por email*/
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        /*
        ->Si el usuario que encontramos no es nulo, entonces lo transformamos en un usuario del dominio
        de springsecurity
        ->En la siguientes líneas estamos configurando que cuando un usuario se loguee(inicie sesión con sus credenciales) 
        springsecurity vaya directamente a el método loadUserByUsername para otorgar los permisos a los que tiene
        el usuariok
         */
        if (usuario != null) {

            /*Usamos la clase que nos provee spring security llamada User, este constructor de la clase User
            nos pide determiandos parámetros:
            username-> usuario.getEmail (recordemos que el username iba a ser el email)
            password-> usuario.getPassword
            Collection (lista de permisos)-> creamos una lista que guarda objetos de la clase GrantedAuthority que contiene los permisos
             */
            List<GrantedAuthority> permisos = new ArrayList();

            /*
            ->creamos un objeto de tipo "GrantedAuthority" que guardamos en la lista
            ->le damos los permisos a cualquier usuario con un rol determinado, y esto se indica con "ROLE_" +roldelusuario,
            notemos que esto se va a ver textualmente como "ROLE_USER"
            ->EN EL OBJETO p GUARDAMOS LOS PERMISOS DEL USUARIO
             */
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString()); //ROLE_USER

            permisos.add(p);

            /*7.1_
            ->a esta altura ya sabemos que el usuario ingresó a la plataforma y ya le dimos los permisos, y vamos a insertar una llamada para atrapar
            a ese usuario que ya está autenticado, y guardarlo en la sesión web
            ->Entonces hacemos una llamada al request (ServletRequestAttribute) que me permite almacenr y acceder a los atributos de la solicitud
            Http, la información se va a guardar y va a viajar en la variable "attr"
            ->Con toda esta línea recuperamos los atributos del request, osea, de la solicitud http.
            ->Una vez que tenemos la solicitud guardada en attr, la vamos a guardar en un objeto de tipo HttpSession, lo que vamos a guardar es el 
            llamado que nos trae attr, y en base a eso, los datos de la sesión de la solicitud http
            ->Luego, en los datos de esta sesión vamos a setear los atributos del usuario que buscamos en la base de datos, que es el usuario que se loguea
            ->Todo esto nos sirve para lograr que cuando se loguee un usuario tipo administrador, nos lleve al dashboard y no al inicio común del usuario.
             */
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession sesion = attr.getRequest().getSession(true);

            sesion.setAttribute("usuariosession", usuario); //en la variable sesion seteamos el atributo "usuariosession" como llave que contiene el valor con todos los datos del objeto usuario que habría traído, osea, el que está autenticado

            User user = new User(usuario.getEmail(), usuario.getPassword(), permisos);

            //finalmente retornamos el usuario del dominio de springsecurity
            return user;
        } else {
            //si no encuentra el usuario, que retorne null
            return null;
        }

    }


}
