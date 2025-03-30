package modelo;

import java.io.Serializable;

public class SorteOuReves implements Serializable {
    private String texto;           // descricao da carta
    private int valor;              // valor a ser adicionado/retirado do saldo do jogador, depende se for sorte ou reves


    // construtor
    public SorteOuReves(String texto, int valor){
        this.texto = texto;
        this.valor = valor;
    }

    // Getters e Setters

    public void setTexto(String texto){
        this.texto = texto;
    }

    public String getTexto(){
        return this.texto;
    }

    public void setValor(int valor){
        this.valor = valor;
    }

    public int getValor(){
        return this.valor;
    }



}
