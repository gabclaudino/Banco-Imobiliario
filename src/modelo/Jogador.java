package modelo;

import javax.swing.*;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Jogador implements Serializable {
    private int indice;                 // indice referente
    private String nome;                // nome
    private int saldo;                  // saldo
    private int posicao;                // posicao no tabuleiro
    private List<Casa> propriedades;    // propriedades compradas
    private boolean preso;              // se esta preso ou nao
    private int rodadasPreso;           // quantas falta para sair da prisao
    private Color cor;                  // cor do peao
    private boolean ferias;             // se esta de ferias ou nao

    // construtor
    public Jogador(int indice, String nome, Color cor) {
        this.indice = indice;
        this.nome = nome;
        this.saldo = 2500;     // inicialmente todos comecam com $2500
        this.posicao = 0;
        this.propriedades = new ArrayList<>();
        this.preso = false;
        this.ferias = false;
        this.rodadasPreso = 0;
        this.cor = cor;
    }


    // metodo para mover o jogador de casa
    public void mover(int numeroDeCasas, Tabuleiro tabuleiro) {
        // salva posicao anterior
        int posicaoAnterior = posicao;
        // calcula a posicao nova
        posicao = (posicao + numeroDeCasas) % tabuleiro.getCasas().size();
        // atualiza a mesma
        tabuleiro.atualizarJogadores(posicaoAnterior, posicao, this);
    }

    // metodo para adicionar propriedade a lista de propriedades
    public void adicionarPropriedade(Casa casa){
        propriedades.add(casa);
    }

    // metodo para tentar sair da prisao
    public void tentarSairDaPrisao() {
        if (rodadasPreso > 0) {
            JOptionPane.showMessageDialog(null, "Você está preso! Tire dois números iguais dos dados para sair!");

            // rolar os dados
            int dado1 = (int) (Math.random() * 6 + 1);
            int dado2 = (int) (Math.random() * 6 + 1);

            JOptionPane.showMessageDialog(null, "Voce tirou " +dado1+ " e " + dado2 + " nos dados!");

            // verifica se os dois numeros foram iguais
            if (dado1 == dado2) {
                // se forem entao sai da prisao, mas so joga na proxima rodada
                JOptionPane.showMessageDialog(null, "Voce foi liberado da prisao!, na proxima rodada voce jogara normalmente");
                preso = false;
                rodadasPreso = 0;
            } else {
                // se nao diminui o tempo de prisao
                JOptionPane.showMessageDialog(null, "Infelizmente nao deu, tente na proxima");
                rodadasPreso--;
                // se chega a zero, na proxima rodada fica livre
                if(rodadasPreso == 0)
                    preso = false;
            }
        }
    }

    // metodo para verificar se o jogador possui todas as propriedades da mesma cor
    public boolean possuiTodasCores(Color cor, Tabuleiro tabuleiro){
        // se alguma propriedade daquela cor tiver outro dono, entao retorna false
        for(Casa casa : tabuleiro.getCasas()){
            if(casa.getCor().equals(cor) && casa.getDono() != this)
                return false;
        }
        return true;
    }

    // metodo para hipotecar a casa mais cara
    public void hipotecarPropriedadeMaisCara(){
        // verifica se ha propriedades para hipoteca
        if(this.getPropriedades().isEmpty()){
            JOptionPane.showMessageDialog(null, "Voce nao possui propriedades para hipoteca!");
            return;
        }

        // procura qual a casa com mais valor de hipoteca
        Casa maisCara = this.getPropriedades().stream().max((p1, p2) -> Integer.compare(p1.getHipoteca(), p2.getHipoteca())).orElse(null);

        if(maisCara != null)
        {
            JOptionPane.showMessageDialog(null, "Hipotecando a propriedade mais cara: " + maisCara.getNome());

            // se houver casas contruidas na propriedade, elas serao removidas
            if(maisCara.getNumCasas() != 0){
                maisCara.removerTodasCasas(this);

            }
            // marca o dono como null
            maisCara.setDono(null);
            // retorna o valor de aluguel inicial
            maisCara.setAluguel(maisCara.getAluguelIni());
        }
        // remove a propriedade da lista de propriedades do jogador
        this.getPropriedades().remove(maisCara);

        // atribui o valor da hipoteca para o saldo do jogador
        assert maisCara != null;
        this.setSaldo(this.getSaldo() + maisCara.getHipoteca());
    }

    // Getters e Setters

    public int getIndice(){
        return this.indice;
    }

    public String getNome() {
        return nome;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo){
        this.saldo = saldo;
    }

    public int getPosicao() {
        return posicao;
    }

    public List<Casa> getPropriedades() {
        return propriedades;
    }

    public boolean getPreso() {
        return this.preso;
    }

    public void setPreso(boolean preso){
        this.preso = preso;
    }

    public boolean getFerias(){
        return this.ferias;
    }

    public void setFerias(boolean ferias){
        this.ferias = ferias;
    }

    public int getRodadasPreso() {
        return rodadasPreso;
    }

    public void setRodadasPreso(int rodadasPreso){
        this.rodadasPreso = rodadasPreso;
    }

    public Color getCor() {
        return cor;
    }




}
