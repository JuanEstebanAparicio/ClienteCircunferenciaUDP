/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.dominio.modelos;

import java.time.LocalDateTime;
/**
 *
 * @author apari
 */
public final class EventoCliente {
    
    private final LocalDateTime fechaHora;
    private final String categoria;
    private final String descripcion;
    
    public EventoCliente(final String categoria, final String descripcion) {
        this.fechaHora = LocalDateTime.now();
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }  
}
