/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.aplicacion.servicios;

import java.util.Objects;
import juanesteban.circunferencia.aplicacion.dto.ConectarCommand;
import juanesteban.circunferencia.aplicacion.dto.DesconectarCommand;
import juanesteban.circunferencia.aplicacion.dto.RespuestaServidorDto;
import juanesteban.circunferencia.aplicacion.excepciones.ConexionRedException;
import juanesteban.circunferencia.aplicacion.mapper.PeticionMapper;
import juanesteban.circunferencia.aplicacion.mapper.RespuestaMapper;
import juanesteban.circunferencia.aplicacion.puertos.entrada.GestionarConexionInputPort;
import juanesteban.circunferencia.dominio.enums.EstadoConexion;
import juanesteban.circunferencia.dominio.enums.TipoRespuesta;
import juanesteban.circunferencia.dominio.modelos.EventoCliente;
import juanesteban.circunferencia.dominio.modelos.RespuestaServidor;
import juanesteban.circunferencia.dominio.puertos.salida.PuertoComunicacionRed;
import juanesteban.circunferencia.dominio.puertos.salida.PuertoNotificacionEventoCliente;
import juanesteban.circunferencia.dominio.vo.Destinatario;
/**
 *
 * @author apari
 */
public final class GestionarConexionService implements GestionarConexionInputPort {

  private final PuertoComunicacionRed comunicacion;
  private final PuertoNotificacionEventoCliente notificador;
  private final PeticionMapper peticionMapper;
  private final RespuestaMapper respuestaMapper;

  public GestionarConexionService(
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
  public RespuestaServidorDto conectar(final ConectarCommand comando) throws ConexionRedException {
    Objects.requireNonNull(comando, "El comando de conexión es obligatorio.");

    final Destinatario servidor =
        peticionMapper.toDestinatario(comando.ipServidor(), comando.puertoServidor());
    final String mensaje = peticionMapper.mensajeConectar();

    final RespuestaServidor respuesta = enviarYNotificar(servidor, mensaje);

    if (respuesta.getTipo() == TipoRespuesta.CONECTADO) {
      notificador.notificarCambioEstado(EstadoConexion.CONECTADO);
    }
    return respuestaMapper.toDto(respuesta);
  }

  @Override
  public RespuestaServidorDto desconectar(final DesconectarCommand comando)
      throws ConexionRedException {
    Objects.requireNonNull(comando, "El comando de desconexión es obligatorio.");

    final Destinatario servidor =
        peticionMapper.toDestinatario(comando.ipServidor(), comando.puertoServidor());
    final String mensaje = peticionMapper.mensajeDesconectar();

    final RespuestaServidor respuesta = enviarYNotificar(servidor, mensaje);

    notificador.notificarCambioEstado(EstadoConexion.DESCONECTADO);
    return respuestaMapper.toDto(respuesta);
  }

  private RespuestaServidor enviarYNotificar(final Destinatario servidor, final String mensaje)
      throws ConexionRedException {
    try {
      final String textoCrudo = comunicacion.enviarYRecibir(servidor, mensaje);
      final RespuestaServidor respuesta = respuestaMapper.toDomain(textoCrudo);
      notificador.notificarEvento(
          new EventoCliente("EVENTO", servidor.endpoint() + " --> " + mensaje + " : " + textoCrudo));
      return respuesta;
    } catch (final ConexionRedException excepcion) {
      notificador.notificarEvento(
          new EventoCliente("ERROR", "Fallo al comunicarse con " + servidor.endpoint() + ": "
              + excepcion.getMessage()));
      throw excepcion;
    }
  }
}