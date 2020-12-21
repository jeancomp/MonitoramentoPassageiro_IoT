package br.ufma.monitoramentopass;

import java.util.Date;

public class LocalizacaoPass {
    String identificador;
    String latitude;
    String longitude;
    String altitude;
    String velocidade;
    String latitudeDestino;
    String longitudeDestino;

    LocalizacaoPass(){ }

    public String getIdentificador() {
        return identificador;
    }
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAltitude() {
        return altitude;
    }
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getVelocidade() {
        return velocidade;
    }
    public void setVelocidade(String velocidade) {
        this.velocidade = velocidade;
    }

    public String getLatitudeDestino() { return latitudeDestino; }
    public void setLatitudeDestino(String latDestino) { this.latitudeDestino = latDestino; }

    public String getLongitudeDestino() { return longitudeDestino; }
    public void setLongitudeDestino(String lonDestino) { this.longitudeDestino = lonDestino; }
}
