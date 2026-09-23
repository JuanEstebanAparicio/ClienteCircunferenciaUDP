/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.adaptadores.red;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import juanesteban.circunferencia.dominio.enums.EstadoConexion;
import juanesteban.circunferencia.dominio.modelos.EventoCliente;
import juanesteban.circunferencia.dominio.puertos.salida.PuertoNotificacionEventoCliente;
/**
 *
 * @author apari
 */
public final class AdaptadorNotificacionEventoCliente implements PuertoNotificacionEventoCliente {

  private final List<ObservadorCliente> observadores = new CopyOnWriteArrayList<>();

  public void registrarObservador(final ObservadorCliente observador) {
    if (Objects.nonNull(observador) && !observadores.contains(observador)) {
      observadores.add(observador);
    }
  }

  public void removerObservador(final ObservadorCliente observador) {
    observadores.remove(observador);
  }

  @Override
  public void notificarEvento(final EventoCliente evento) {
    Objects.requireNonNull(evento, "El evento es obligatorio.");
    for (final ObservadorCliente observador : observadores) {
      observador.onEvento(evento);
    }
  }

  @Override
  public void notificarCambioEstado(final EstadoConexion nuevoEstado) {
    Objects.requireNonNull(nuevoEstado, "El estado es obligatorio.");
    for (final ObservadorCliente observador : observadores) {
      observador.onCambioEstado(nuevoEstado);
    }
  }
}