/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.aplicacion.puertos.entrada;

import juanesteban.circunferencia.aplicacion.dto.ConectarCommand;
import juanesteban.circunferencia.aplicacion.dto.DesconectarCommand;
import juanesteban.circunferencia.aplicacion.dto.RespuestaServidorDto;
import juanesteban.circunferencia.aplicacion.excepciones.ConexionRedException;
/**
 *
 * @author apari
 */
public interface GestionarConexionInputPort {

  RespuestaServidorDto conectar(ConectarCommand comando) throws ConexionRedException;

  RespuestaServidorDto desconectar(DesconectarCommand comando) throws ConexionRedException;
}