package juanesteban.circunferencia.dominio.modelos;

import java.util.Objects;
import juanesteban.circunferencia.dominio.enums.TipoRespuesta;
import juanesteban.circunferencia.dominio.excepciones.RespuestaInvalidaException;

/** Modelo inmutable del dominio que representa una respuesta del servidor ya interpretada. */
public final class RespuestaServidor {

  private final TipoRespuesta tipo;
  private final String longitudFormateada;
  private final String mensaje;

  public RespuestaServidor(
      final TipoRespuesta tipo, final String longitudFormateada, final String mensaje) {
    if (Objects.isNull(tipo)) {
      throw new RespuestaInvalidaException("El tipo de respuesta del servidor es obligatorio.");
    }
    if (tipo == TipoRespuesta.OK_CALCULO && Objects.isNull(longitudFormateada)) {
      throw new RespuestaInvalidaException(
          "Una respuesta de cálculo exitosa debe incluir la longitud.");
    }
    this.tipo = tipo;
    this.longitudFormateada = longitudFormateada;
    this.mensaje = mensaje;
  }

  public TipoRespuesta getTipo() {
    return tipo;
  }

  public String getLongitudFormateada() {
    return longitudFormateada;
  }

  public String getMensaje() {
    return mensaje;
  }
}