/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.dominio.puertos.salida;

import juanesteban.circunferencia.dominio.enums.EstadoConexion;
import juanesteban.circunferencia.dominio.modelos.EventoCliente;
/**
 *
 * @author apari
 */
public interface PuertoNotificacionEventoCliente {

  void notificarEvento(EventoCliente evento);

  void notificarCambioEstado(EstadoConexion nuevoEstado);
}
