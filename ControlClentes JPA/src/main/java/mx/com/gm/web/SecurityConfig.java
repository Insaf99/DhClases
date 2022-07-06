package mx.com.gm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    //inyeccion del servicio de usuarios
    private UserDetailsService userDetailsService;

    @Bean
    //creamos un nuevo objeto del tipo BCryptPasswordEncoder
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    //indicar que vamos a usar la implementacion de userdetailservice que creamos en el servicio
    // y tambien el tipo de codificacion password enconder ya que es la misma que difinimos en el
    // metodo anterior.
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception{
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    //@Override
    /*
     * Crea 2 usuarios en memoria y les asigna el rol y una contraseña.
     *
    *****SE BORRA EL METODO PORQUE YA NO ESTAMOS TRABAJANDO EN MEMORIA*****
    *
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}123")
                .roles("ADMIN","USER")
                .and()
                .withUser("user")
                .password("{noop}123")
                .roles("USER")
        ;
    }*/
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/editar/**", "/agregar/**", "/eliminar")
                .hasRole("ADMIN")
                .antMatchers("/")
                .hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .exceptionHandling().accessDeniedPage("/errores/403");
    }
}
