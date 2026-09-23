/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.aplicacion.puertos.entrada;

import juanesteban.circunferencia.aplicacion.dto.EnviarCalculoCommand;
import juanesteban.circunferencia.aplicacion.dto.RespuestaServidorDto;
import juanesteban.circunferencia.aplicacion.excepciones.ConexionRedException;
/**
 *
 * @author apari
 */
public interface EnviarCalculoPort {
    
      RespuestaServidorDto calcular(EnviarCalculoCommand comando) throws ConexionRedException;
}
