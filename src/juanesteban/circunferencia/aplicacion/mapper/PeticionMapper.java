/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.aplicacion.mapper;

import java.util.Objects;
import juanesteban.circunferencia.aplicacion.excepciones.ComandoInvalidoException;
import juanesteban.circunferencia.dominio.vo.Destinatario;
import juanesteban.circunferencia.dominio.vo.Radio;
/**
 *
 * @author apari
 */
public class PeticionMapper {
    
    private static final String MENSAJE_CONECTAR = "CONECTAR";
    private static final String MENSAJE_DESCONECTAR = "DESCONECTAR";
    
    public Destinatario toDestinatario(final String ipServidor, final int puertoServidor) {
    return new Destinatario(ipServidor, puertoServidor);
  }

  public String mensajeConectar() {
    return MENSAJE_CONECTAR;
  }

  public String mensajeDesconectar() {
    return MENSAJE_DESCONECTAR;
  }

  public String mensajeCalcular(final double valorRadio) {
    final Radio radio = new Radio(valorRadio);
    if (Objects.isNull(radio)) {
      throw new ComandoInvalidoException("El radio es obligatorio para solicitar un cálculo.");
    }
    return "CALCULAR;" + radio.valor();
  }
}