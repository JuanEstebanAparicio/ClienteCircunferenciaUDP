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
public class DominioException extends RuntimeException {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    public DominioException(final String mensaje) {
        super(mensaje);
    }
    
    public DominioException(final String mensaje, final Throwable causa){
        super(mensaje, causa);
    }
    
}
