/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.dominio.puertos.salida;

import juanesteban.circunferencia.aplicacion.excepciones.ConexionRedException;
import juanesteban.circunferencia.dominio.vo.Destinatario;
/**
 *
 * @author apari
 */
public interface PuertoComunicacionRed {

  String enviarYRecibir(Destinatario servidor, String mensaje) throws ConexionRedException;
}