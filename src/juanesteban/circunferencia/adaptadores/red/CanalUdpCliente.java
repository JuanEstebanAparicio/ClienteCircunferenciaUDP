/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.adaptadores.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 *
 * @author apari
 */
public class CanalUdpCliente {

  private static final int BUFFER_SIZE = 2048;
  private static final int TIMEOUT_MS = 3000;

  private DatagramSocket socket;

  public synchronized void abrir() throws SocketException {
    if (Objects.nonNull(socket) && !socket.isClosed()) {
      return;
    }
    socket = new DatagramSocket();
    socket.setSoTimeout(TIMEOUT_MS);
  }

  public synchronized void cerrar() {
    if (Objects.nonNull(socket) && !socket.isClosed()) {
      socket.close();
    }
    socket = null;
  }

  public synchronized boolean isAbierto() {
    return Objects.nonNull(socket) && !socket.isClosed();
  }

  /**
   * Envía un mensaje de texto al servidor y espera su respuesta hasta el tiempo límite configurado.
   *
   * @throws IOException si el socket está cerrado, el host es inalcanzable o se agota el tiempo de
   *     espera (SocketTimeoutException, subclase de IOException).
   */
  public synchronized String enviarYRecibir(final String ip, final int puerto, final String mensaje)
      throws IOException {
    abrir();

    final byte[] datosEnvio = mensaje.getBytes(StandardCharsets.UTF_8);
    final InetAddress direccion = InetAddress.getByName(ip);
    final DatagramPacket paqueteEnvio = new DatagramPacket(datosEnvio, datosEnvio.length, direccion, puerto);
    socket.send(paqueteEnvio);

    final byte[] bufferRespuesta = new byte[BUFFER_SIZE];
    final DatagramPacket paqueteRespuesta = new DatagramPacket(bufferRespuesta, bufferRespuesta.length);
    socket.receive(paqueteRespuesta);

    return new String(
        paqueteRespuesta.getData(), 0, paqueteRespuesta.getLength(), StandardCharsets.UTF_8);
  }
}