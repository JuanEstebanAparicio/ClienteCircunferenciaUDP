/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.aplicacion.excepciones;

import java.io.Serial;
/**
 *
 * @author apari
 */
public final class ConexionRedException extends Exception {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
     public ConexionRedException(final String mensaje, final Throwable causa) {
        super(mensaje, causa);
    }
}
