/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package juanesteban.circunferencia;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import juanesteban.circunferencia.adaptadores.red.AdaptadorComunicacionRed;
import juanesteban.circunferencia.adaptadores.red.AdaptadorNotificacionEventoCliente;
import juanesteban.circunferencia.adaptadores.red.CanalUdpCliente;
import juanesteban.circunferencia.aplicacion.mapper.PeticionMapper;
import juanesteban.circunferencia.aplicacion.mapper.RespuestaMapper;
import juanesteban.circunferencia.aplicacion.puertos.entrada.EnviarCalculoInputPort;
import juanesteban.circunferencia.aplicacion.puertos.entrada.GestionarConexionInputPort;
import juanesteban.circunferencia.aplicacion.servicios.EnviarCalculoService;
import juanesteban.circunferencia.aplicacion.servicios.GestionarConexionService;
import juanesteban.circunferencia.dominio.puertos.salida.PuertoComunicacionRed;
import juanesteban.circunferencia.entrypoint.gui.ClienteFrame;
/**
 *
 * @author apari
 */
public class principal {

  private static final System.Logger LOG = System.getLogger(principal.class.getName());

  private principal() {
    // Evita instanciación: clase de arranque estática
  }

  public static void main(final String[] args) {
    aplicarLookAndFeel();

    // 1. Infraestructura de red
    final CanalUdpCliente canalUdp = new CanalUdpCliente();
    final AdaptadorNotificacionEventoCliente notificador = new AdaptadorNotificacionEventoCliente();
    final PuertoComunicacionRed comunicacion = new AdaptadorComunicacionRed(canalUdp);

    // 2. Mappers de aplicación
    final PeticionMapper peticionMapper = new PeticionMapper();
    final RespuestaMapper respuestaMapper = new RespuestaMapper();

    // 3. Casos de uso (servicios de aplicación)
    final GestionarConexionInputPort gestionarConexionPort =
        new GestionarConexionService(comunicacion, notificador, peticionMapper, respuestaMapper);

    final EnviarCalculoInputPort enviarCalculoPort =
        new EnviarCalculoService(comunicacion, notificador, peticionMapper, respuestaMapper);

    // 4. Lanzar GUI en el Event Dispatch Thread de Swing
    SwingUtilities.invokeLater(
        () -> {
          final ClienteFrame frame = new ClienteFrame(gestionarConexionPort, enviarCalculoPort);
          notificador.registrarObservador(frame);
          frame.setVisible(true);
        });
  }

  private static void aplicarLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (final ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | UnsupportedLookAndFeelException excepcion) {
      LOG.log(
          System.Logger.Level.DEBUG,
          "No fue posible aplicar la apariencia del sistema; se usará la predeterminada.",
          excepcion);
    }
  }
}