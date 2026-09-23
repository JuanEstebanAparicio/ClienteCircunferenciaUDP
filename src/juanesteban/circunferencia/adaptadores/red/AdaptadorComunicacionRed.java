/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.adaptadores.red;

import java.io.IOException;
import java.util.Objects;
import juanesteban.circunferencia.aplicacion.excepciones.ConexionRedException;
import juanesteban.circunferencia.dominio.puertos.salida.PuertoComunicacionRed;
import juanesteban.circunferencia.dominio.vo.Destinatario;

/**
 *
 * @author apari
 */
public final class AdaptadorComunicacionRed implements PuertoComunicacionRed {

  private final CanalUdpCliente canalUdp;

  public AdaptadorComunicacionRed(final CanalUdpCliente canalUdp) {
    this.canalUdp = Objects.requireNonNull(canalUdp, "El canal UDP es obligatorio.");
  }

  @Override
  public String enviarYRecibir(final Destinatario servidor, final String mensaje)
      throws ConexionRedException {
    try {
      return canalUdp.enviarYRecibir(servidor.ip(), servidor.puerto(), mensaje);
    } catch (final IOException excepcion) {
      throw new ConexionRedException(
          "No se pudo comunicar con el servidor " + servidor.endpoint() + ": "
              + excepcion.getMessage(),
          excepcion);
    }
  }
}