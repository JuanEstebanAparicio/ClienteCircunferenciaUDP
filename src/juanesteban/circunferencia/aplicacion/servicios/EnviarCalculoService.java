package juanesteban.circunferencia.aplicacion.servicios;

import java.util.Objects;
import juanesteban.circunferencia.aplicacion.dto.EnviarCalculoCommand;
import juanesteban.circunferencia.aplicacion.dto.RespuestaServidorDto;
import juanesteban.circunferencia.aplicacion.excepciones.ConexionRedException;
import juanesteban.circunferencia.aplicacion.mapper.PeticionMapper;
import juanesteban.circunferencia.aplicacion.mapper.RespuestaMapper;
import juanesteban.circunferencia.aplicacion.puertos.entrada.EnviarCalculoInputPort;
import juanesteban.circunferencia.dominio.modelos.EventoCliente;
import juanesteban.circunferencia.dominio.modelos.RespuestaServidor;
import juanesteban.circunferencia.dominio.puertos.salida.PuertoComunicacionRed;
import juanesteban.circunferencia.dominio.puertos.salida.PuertoNotificacionEventoCliente;
import juanesteban.circunferencia.dominio.vo.Destinatario;

/**
 * Servicio de aplicación que implementa el caso de uso de solicitar al servidor el cálculo de la
 * circunferencia. Responsabilidad única: construir la petición, coordinar el envío y traducir la
 * respuesta del servidor a un DTO para la capa de presentación.
 */
public final class EnviarCalculoService implements EnviarCalculoInputPort {

  private final PuertoComunicacionRed comunicacion;
  private final PuertoNotificacionEventoCliente notificador;
  private final PeticionMapper peticionMapper;
  private final RespuestaMapper respuestaMapper;

  public EnviarCalculoService(
      final PuertoComunicacionRed comunicacion,
      final PuertoNotificacionEventoCliente notificador,
      final PeticionMapper peticionMapper,
      final RespuestaMapper respuestaMapper) {
    this.comunicacion = Objects.requireNonNull(comunicacion, "La comunicación es obligatoria.");
    this.notificador = Objects.requireNonNull(notificador, "El notificador es obligatorio.");
    this.peticionMapper =
        Objects.requireNonNull(peticionMapper, "El mapper de petición es obligatorio.");
    this.respuestaMapper =
        Objects.requireNonNull(respuestaMapper, "El mapper de respuesta es obligatorio.");
  }

  @Override
  public RespuestaServidorDto calcular(final EnviarCalculoCommand comando)
      throws ConexionRedException {
    Objects.requireNonNull(comando, "El comando de cálculo es obligatorio.");

    final Destinatario servidor =
        peticionMapper.toDestinatario(comando.ipServidor(), comando.puertoServidor());
    final String mensaje = peticionMapper.mensajeCalcular(comando.radio());

    try {
      final String textoCrudo = comunicacion.enviarYRecibir(servidor, mensaje);
      final RespuestaServidor respuesta = respuestaMapper.toDomain(textoCrudo);

      notificador.notificarEvento(
          new EventoCliente("EVENTO", servidor.endpoint() + " --> " + mensaje + " : " + textoCrudo));

      return respuestaMapper.toDto(respuesta);

    } catch (final ConexionRedException excepcion) {
      notificador.notificarEvento(
          new EventoCliente(
              "ERROR",
              "Fallo al solicitar el cálculo a " + servidor.endpoint() + ": "
                  + excepcion.getMessage()));
      throw excepcion;
    }
  }
}