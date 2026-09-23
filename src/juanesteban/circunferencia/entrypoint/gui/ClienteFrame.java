/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.entrypoint.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serial;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import juanesteban.circunferencia.adaptadores.red.ObservadorCliente;
import juanesteban.circunferencia.aplicacion.dto.ConectarCommand;
import juanesteban.circunferencia.aplicacion.dto.DesconectarCommand;
import juanesteban.circunferencia.aplicacion.dto.EnviarCalculoCommand;
import juanesteban.circunferencia.aplicacion.dto.RespuestaServidorDto;
import juanesteban.circunferencia.aplicacion.excepciones.ConexionRedException;
import juanesteban.circunferencia.aplicacion.puertos.entrada.EnviarCalculoInputPort;
import juanesteban.circunferencia.aplicacion.puertos.entrada.GestionarConexionInputPort;
import juanesteban.circunferencia.dominio.enums.EstadoConexion;
import juanesteban.circunferencia.dominio.excepciones.DominioException;
import juanesteban.circunferencia.dominio.modelos.EventoCliente;
/**
 *
 * @author apari
 */
public class ClienteFrame extends JFrame implements ObservadorCliente {

  @Serial
  private static final long serialVersionUID = 1L;

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final Color COLOR_ACTIVO = new Color(46, 125, 50);
  private static final Color COLOR_INACTIVO = new Color(198, 40, 40);

  private final transient GestionarConexionInputPort gestionarConexionPort;
  private final transient EnviarCalculoInputPort enviarCalculoPort;

  private JTextField txtIpServidor;
  private JTextField txtPuertoServidor;
  private JButton btnConectarDesconectar;
  private JLabel lblEstado;
  private JTextField txtRadio;
  private JButton btnCalcular;
  private JLabel lblResultado;
  private JTextArea txtLogs;

  private volatile boolean conectado = false;

  public ClienteFrame(
      final GestionarConexionInputPort gestionarConexionPort,
      final EnviarCalculoInputPort enviarCalculoPort) {
    super("Cliente UDP - Cálculo de Circunferencia (Arquitectura Hexagonal / SOLID)");
    this.gestionarConexionPort =
        Objects.requireNonNull(
            gestionarConexionPort, "El puerto de gestión de conexión es obligatorio.");
    this.enviarCalculoPort =
        Objects.requireNonNull(enviarCalculoPort, "El puerto de cálculo es obligatorio.");
    initUI();
  }

  private void initUI() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(760, 560);
    setMinimumSize(new Dimension(680, 460));
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(10, 10));

    add(crearPanelNorte(), BorderLayout.NORTH);
    add(crearPanelCentral(), BorderLayout.CENTER);

    agregarLineaLogInicial();
  }

  private JPanel crearPanelNorte() {
    final JPanel panelNorte = new JPanel(new BorderLayout(5, 5));
    panelNorte.setBorder(new EmptyBorder(12, 12, 5, 12));

    final JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
    final JLabel lblTitulo = new JLabel("Cliente de Cálculo de Circunferencia (Protocolo UDP)");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblTitulo.setForeground(new Color(33, 33, 33));
    panelTitulo.add(lblTitulo);
    panelNorte.add(panelTitulo, BorderLayout.NORTH);
    panelNorte.add(crearPanelConexion(), BorderLayout.CENTER);
    return panelNorte;
  }

  private JPanel crearPanelConexion() {
    final JPanel panelConfig = new JPanel(new GridBagLayout());
    panelConfig.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Conexión con el Servidor",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)),
            new EmptyBorder(8, 10, 8, 10)));

    final GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(6, 6, 6, 6);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    panelConfig.add(labelBold("IP del Servidor:"), gbc);

    gbc.gridx = 1;
    gbc.weightx = 0.3;
    txtIpServidor = new JTextField("127.0.0.1");
    txtIpServidor.setFont(new Font("Consolas", Font.PLAIN, 13));
    panelConfig.add(txtIpServidor, gbc);

    gbc.gridx = 2;
    gbc.weightx = 0.0;
    panelConfig.add(labelBold("Puerto:"), gbc);

    gbc.gridx = 3;
    gbc.weightx = 0.2;
    txtPuertoServidor = new JTextField("9015");
    txtPuertoServidor.setFont(new Font("Consolas", Font.PLAIN, 13));
    panelConfig.add(txtPuertoServidor, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0.0;
    panelConfig.add(labelBold("Estado:"), gbc);

    gbc.gridx = 1;
    gbc.weightx = 0.3;
    lblEstado = new JLabel("● DESCONECTADO");
    lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
    lblEstado.setForeground(COLOR_INACTIVO);
    panelConfig.add(lblEstado, gbc);

    gbc.gridx = 2;
    gbc.gridwidth = 2;
    gbc.weightx = 0.5;
    btnConectarDesconectar = new JButton("Conectar");
    btnConectarDesconectar.setFont(new Font("Segoe UI", Font.BOLD, 13));
    btnConectarDesconectar.setBackground(COLOR_ACTIVO);
    btnConectarDesconectar.setForeground(Color.WHITE);
    btnConectarDesconectar.setFocusPainted(false);
    btnConectarDesconectar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnConectarDesconectar.addActionListener(e -> alternarConexion());
    panelConfig.add(btnConectarDesconectar, gbc);

    return panelConfig;
  }

  private JPanel crearPanelCentral() {
    final JPanel panelCentral = new JPanel(new BorderLayout(8, 8));
    panelCentral.setBorder(new EmptyBorder(0, 12, 12, 12));

    panelCentral.add(crearPanelCalculo(), BorderLayout.NORTH);

    final JPanel panelLogHeader = new JPanel(new BorderLayout());
    final JLabel lblLogsTitulo = new JLabel("Registro de Peticiones y Respuestas:");
    lblLogsTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
    panelLogHeader.add(lblLogsTitulo, BorderLayout.WEST);

    final JButton btnLimpiarLog = new JButton("Limpiar Log");
    btnLimpiarLog.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    btnLimpiarLog.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnLimpiarLog.addActionListener(e -> txtLogs.setText(""));
    panelLogHeader.add(btnLimpiarLog, BorderLayout.EAST);

    final JPanel panelLog = new JPanel(new BorderLayout(5, 5));
    panelLog.add(panelLogHeader, BorderLayout.NORTH);

    txtLogs = new JTextArea();
    txtLogs.setEditable(false);
    txtLogs.setFont(new Font("Consolas", Font.PLAIN, 12));
    txtLogs.setBackground(new Color(250, 250, 250));
    txtLogs.setForeground(new Color(30, 30, 30));
    txtLogs.setLineWrap(true);
    txtLogs.setWrapStyleWord(true);

    final JScrollPane scrollLogs = new JScrollPane(txtLogs);
    scrollLogs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    panelLog.add(scrollLogs, BorderLayout.CENTER);

    panelCentral.add(panelLog, BorderLayout.CENTER);
    return panelCentral;
  }

  private JPanel crearPanelCalculo() {
    final JPanel panelCalculo = new JPanel(new GridBagLayout());
    panelCalculo.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Cálculo de la Circunferencia",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)),
            new EmptyBorder(8, 10, 8, 10)));

    final GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(6, 6, 6, 6);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    panelCalculo.add(labelBold("Radio:"), gbc);

    gbc.gridx = 1;
    gbc.weightx = 0.3;
    txtRadio = new JTextField();
    txtRadio.setFont(new Font("Consolas", Font.PLAIN, 13));
    panelCalculo.add(txtRadio, gbc);

    gbc.gridx = 2;
    gbc.weightx = 0.0;
    btnCalcular = new JButton("Calcular");
    btnCalcular.setFont(new Font("Segoe UI", Font.BOLD, 13));
    btnCalcular.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnCalcular.setEnabled(false);
    btnCalcular.addActionListener(e -> solicitarCalculo());
    panelCalculo.add(btnCalcular, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.weightx = 1.0;
    lblResultado = new JLabel("Longitud de la circunferencia: —");
    lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 14));
    panelCalculo.add(lblResultado, gbc);

    return panelCalculo;
  }

  private static JLabel labelBold(final String texto) {
    final JLabel lbl = new JLabel(texto);
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
    return lbl;
  }

  private void alternarConexion() {
    if (!conectado) {
      conectar();
    } else {
      desconectar();
    }
  }

  private void conectar() {
    final String ip = txtIpServidor.getText().trim();
    final int puerto;
    try {
      puerto = Integer.parseInt(txtPuertoServidor.getText().trim());
    } catch (final NumberFormatException excepcion) {
      JOptionPane.showMessageDialog(
          this, "El puerto ingresado no es válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
      return;
    }

    btnConectarDesconectar.setEnabled(false);
    new Thread(
            () -> {
              try {
                gestionarConexionPort.conectar(new ConectarCommand(ip, puerto));
              } catch (final ConexionRedException | DominioException excepcion) {
                mostrarErrorDialog("No se pudo conectar: " + excepcion.getMessage());
              } finally {
                SwingUtilities.invokeLater(() -> btnConectarDesconectar.setEnabled(true));
              }
            },
            "Thread-ClienteFrame-Conectar")
        .start();
  }

  private void desconectar() {
    final String ip = txtIpServidor.getText().trim();
    final int puerto = Integer.parseInt(txtPuertoServidor.getText().trim());

    btnConectarDesconectar.setEnabled(false);
    new Thread(
            () -> {
              try {
                gestionarConexionPort.desconectar(new DesconectarCommand(ip, puerto));
              } catch (final ConexionRedException | DominioException excepcion) {
                mostrarErrorDialog("No se pudo desconectar: " + excepcion.getMessage());
              } finally {
                SwingUtilities.invokeLater(() -> btnConectarDesconectar.setEnabled(true));
              }
            },
            "Thread-ClienteFrame-Desconectar")
        .start();
  }

  private void solicitarCalculo() {
    final String ip = txtIpServidor.getText().trim();
    final int puerto = Integer.parseInt(txtPuertoServidor.getText().trim());
    final double valorRadio;
    try {
      valorRadio = Double.parseDouble(txtRadio.getText().trim().replace(',', '.'));
    } catch (final NumberFormatException excepcion) {
      JOptionPane.showMessageDialog(
          this, "El radio debe ser un número.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
      return;
    }

    btnCalcular.setEnabled(false);
    new Thread(
            () -> {
              try {
                final RespuestaServidorDto respuesta =
                    enviarCalculoPort.calcular(new EnviarCalculoCommand(ip, puerto, valorRadio));
                mostrarResultado(respuesta);
              } catch (final ConexionRedException | DominioException excepcion) {
                mostrarErrorDialog("No se pudo calcular: " + excepcion.getMessage());
              } finally {
                SwingUtilities.invokeLater(() -> btnCalcular.setEnabled(true));
              }
            },
            "Thread-ClienteFrame-Calcular")
        .start();
  }

  private void mostrarResultado(final RespuestaServidorDto respuesta) {
    SwingUtilities.invokeLater(
        () -> {
          if ("OK_CALCULO".equals(respuesta.tipo())) {
            lblResultado.setText(
                "Longitud de la circunferencia: " + respuesta.longitudFormateada());
          } else {
            lblResultado.setText("Longitud de la circunferencia: —");
            JOptionPane.showMessageDialog(
                this, respuesta.mensaje(), "Respuesta del Servidor", JOptionPane.WARNING_MESSAGE);
          }
        });
  }

  private void mostrarErrorDialog(final String mensaje) {
    SwingUtilities.invokeLater(
        () ->
            JOptionPane.showMessageDialog(
                this, mensaje, "Error de Comunicación", JOptionPane.ERROR_MESSAGE));
  }

  @Override
  public void onEvento(final EventoCliente evento) {
    if (Objects.isNull(evento)) {
      return;
    }
    final String timestamp = evento.getFechaHora().format(DATE_FORMAT);
    final String linea =
        String.format(
            "%s: [%s] %s%n", timestamp, evento.getCategoria(), evento.getDescripcion());
    SwingUtilities.invokeLater(
        () -> {
          txtLogs.append(linea);
          txtLogs.setCaretPosition(txtLogs.getDocument().getLength());
        });
  }

  @Override
  public void onCambioEstado(final EstadoConexion nuevoEstado) {
    conectado = nuevoEstado == EstadoConexion.CONECTADO;
    SwingUtilities.invokeLater(
        () -> {
          if (conectado) {
            txtIpServidor.setEditable(false);
            txtPuertoServidor.setEditable(false);
            btnConectarDesconectar.setText("Desconectar");
            btnConectarDesconectar.setBackground(COLOR_INACTIVO);
            lblEstado.setText("● CONECTADO");
            lblEstado.setForeground(COLOR_ACTIVO);
            btnCalcular.setEnabled(true);
          } else {
            txtIpServidor.setEditable(true);
            txtPuertoServidor.setEditable(true);
            btnConectarDesconectar.setText("Conectar");
            btnConectarDesconectar.setBackground(COLOR_ACTIVO);
            lblEstado.setText("● DESCONECTADO");
            lblEstado.setForeground(COLOR_INACTIVO);
            btnCalcular.setEnabled(false);
          }
        });
  }

  private void agregarLineaLogInicial() {
    final String linea =
        "Aplicación lista. Ingrese la IP y el puerto del servidor y presione 'Conectar'." + System.lineSeparator();
    SwingUtilities.invokeLater(
        () -> {
          txtLogs.append(linea);
          txtLogs.setCaretPosition(txtLogs.getDocument().getLength());
        });
  }
}