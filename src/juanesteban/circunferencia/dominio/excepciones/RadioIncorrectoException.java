/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.dominio.excepciones;

import java.io.Serial;
/**
 *
 * @author apari
 */
public class RadioIncorrectoException extends DominioException {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
     public RadioIncorrectoException(final String mensaje) {
        super(mensaje);
  }
}
