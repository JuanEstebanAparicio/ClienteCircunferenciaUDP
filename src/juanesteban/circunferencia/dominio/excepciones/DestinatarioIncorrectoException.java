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
public class DestinatarioIncorrectoException extends DominioException {
    @Serial
    private static final long serialVersionUID = 1L;
    
    public DestinatarioIncorrectoException (final String mensaje) {
        super(mensaje);
    } 
}
