/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juanesteban.circunferencia.aplicacion.dto;

/**
 *
 * @author apari
 */
public record EnviarCalculoCommand(String ipServidor, int puertoServidor, double radio) {
}
