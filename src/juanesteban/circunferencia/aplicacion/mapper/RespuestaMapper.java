/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.aplicacion.mapper;

import java.util.Locale;
import java.util.Objects;
import juanesteban.circunferencia.aplicacion.dto.RespuestaServidorDto;
import juanesteban.circunferencia.dominio.enums.TipoRespuesta;
import juanesteban.circunferencia.dominio.excepciones.RespuestaInvalidaException;
import juanesteban.circunferencia.dominio.modelos.RespuestaServidor;
/**
 *
 * @author apari
 */
public final class RespuestaMapper {

  public RespuestaServidor toDomain(final String textoCrudo) {
    if (Objects.isNull(textoCrudo) || textoCrudo.isBlank()) {
      throw new RespuestaInvalidaException("El servidor envió una respuesta vacía.");
    }

    final String[] partes = textoCrudo.trim().split(";", -1);
    final String encabezado = partes[0].toUpperCase(Locale.ROOT);

    return switch (encabezado) {
      case "CONECTADO_OK" ->
          new RespuestaServidor(TipoRespuesta.CONECTADO, null, unirResto(partes));
      case "DESCONECTADO_OK" ->
          new RespuestaServidor(TipoRespuesta.DESCONECTADO, null, unirResto(partes));
      case "OK_CALCULO" -> toDomainCalculo(partes);
      case "ERROR" -> new RespuestaServidor(TipoRespuesta.ERROR, null, unirResto(partes));
      default ->
          throw new RespuestaInvalidaException(
              "El servidor envió una respuesta con un formato desconocido: " + textoCrudo);
    };
  }

  private RespuestaServidor toDomainCalculo(final String[] partes) {
    if (partes.length < 3) {
      throw new RespuestaInvalidaException(
          "La respuesta de cálculo del servidor tiene un formato incompleto.");
    }
    return new RespuestaServidor(TipoRespuesta.OK_CALCULO, partes[1], partes[2]);
  }

  private String unirResto(final String[] partes) {
    if (partes.length < 2) {
      return "";
    }
    return String.join(";", java.util.Arrays.copyOfRange(partes, 1, partes.length));
  }

  public RespuestaServidorDto toDto(final RespuestaServidor respuesta) {
    if (Objects.isNull(respuesta)) {
      throw new RespuestaInvalidaException("No se puede convertir una respuesta nula.");
    }
    return new RespuestaServidorDto(
        respuesta.getTipo().name(), respuesta.getLongitudFormateada(), respuesta.getMensaje());
  }
}