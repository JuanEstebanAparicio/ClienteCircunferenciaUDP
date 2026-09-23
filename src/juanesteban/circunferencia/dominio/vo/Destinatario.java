/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.dominio.vo;

import java.util.Objects;
import juanesteban.circunferencia.dominio.excepciones.DestinatarioIncorrectoException;
import juanesteban.circunferencia.dominio.excepciones.PuertoIncorrectoException;
/**
 *
 * @author apari
 */
public record Destinatario(String ip, int puerto) {
    
    public Destinatario {
        if (Objects.isNull(ip) || ip.isBlank()) {
            throw new DestinatarioIncorrectoException(
            "La direccion IP del servidor no puede ser nula ni vacia.");
        }
        if (puerto <= 0 || puerto > 65535) {
            throw new PuertoIncorrectoException("El puerto del servidor debe ser un numero entre 1 y 65535"); 
        }
        ip = ip.trim();
    }
    public String endpoint() {
        return ip + ":" + puerto;
    }
}
