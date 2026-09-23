/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.adaptadores.red;

import juanesteban.circunferencia.dominio.enums.EstadoConexion;
import juanesteban.circunferencia.dominio.modelos.EventoCliente;
/**
 *
 * @author apari
 */
public interface ObservadorCliente {

  void onEvento(EventoCliente evento);

  void onCambioEstado(EstadoConexion nuevoEstado);
}
