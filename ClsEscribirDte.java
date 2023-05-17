/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidad.ClsDteitentificacion;

/**
 *
 * @author Usuario
 */
public class ClsEscribirDte {
   
    public ClsDteitentificacion obtenerIdentificacion(String codigoGeneracion) {
        ClsDteObtenerDte dteObtener = new ClsDteObtenerDte();
        ClsDteitentificacion identificacion = dteObtener.obtenerIdentificacion(codigoGeneracion);
        return identificacion;
    }

    public ClsDteitentificacion obtenerIdentificacionConGetters(String codigoGeneracion) {
        ClsDteitentificacion identificacion = obtenerIdentificacion(codigoGeneracion);

        String ambiente = identificacion.getAmbiente();
        String codigo = identificacion.getCodigoGeneracion();
        String fecEmi = identificacion.getFecEmi();
        String horEmi = identificacion.getHorEmi();
        String motivoContin = identificacion.getMotivoContin();
        String numeroControl = identificacion.getNumeroControl();
        String tipoContingencia = identificacion.getTipoContingencia();
        String tipoDte = identificacion.getTipoDte();
        int tipoModelo = identificacion.getTipoModelo();
        String tipoMoneda = identificacion.getTipoMoneda();
        int tipoOperacion = identificacion.getTipoOperacion();
        int version = identificacion.getVersion();

        ClsDteitentificacion identificacionConGetters = new ClsDteitentificacion(
                ambiente,
                codigo,
                fecEmi,
                horEmi,
                motivoContin,
                numeroControl,
                tipoContingencia,
                tipoDte,
                tipoModelo,
                tipoMoneda,
                tipoOperacion,
                version
        );

        return identificacionConGetters;
    }
}

    

