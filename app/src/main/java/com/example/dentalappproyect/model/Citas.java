package com.example.dentalappproyect.model;

import java.util.Date;

public class Citas {
    private String uid;
    private String paciente;
    private String dentista;
    private String fecha;
    private String hora;
    private String notas;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getDentista() {
        return dentista;
    }

    public void setDentista(String dentista) {
        this.dentista = dentista;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {

        return "Paciente: " + paciente + " Dentista: " + dentista + " Fecha: "+ fecha + " Notas: " + notas;
    }


}
