package modelo;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;


public class Tabuleiro implements Serializable {
    private static Tabuleiro instancia;         // instancia do padrao Singleton
    private List<Casa> casas;                   // lista de casas do tabuleiro
    private List<Jogador> jogadores;            // lista de jogadores do tabuleiro
    private List<SorteOuReves> cartas;          // lista de cartas de Sorte ou Reves

    // construtor
    public Tabuleiro() {
        casas = new ArrayList<>();
        jogadores = new ArrayList<>();
        cartas = new ArrayList<>();


        inicializarCasas();
        inicializarJogadores();
        inicializarCartas();
    }

    // metodo de instancia unica do padrao Singleton
    public static synchronized Tabuleiro getInstancia() {
        if (instancia == null) {
            instancia = new Tabuleiro();
        }
        return instancia;
    }

    // metodo para iniciar as casas, cada uma com suas propriedades especificas
    private void inicializarCasas() {
        // casas topo - 10
        casas.add(new Casa("INÍCIO", 0, Color.BLACK, "INICIO", 0, 0, 0, 1, 0, 0));
        casas.add(new Casa("RUA A", 150, Color.RED, "TERRENO", 1, 150, 0, 1, 20, 100));
        casas.add(new Casa("RUA B", 200, Color.RED, "TERRENO", 2, 225, 0, 1, 30, 120));
        casas.add(new Casa("RUA C", 250, Color.RED, "TERRENO", 3, 300, 0, 1, 40, 150));
        casas.add(new Casa("MULTA", 350, Color.BLACK, "MULTA", 4, 375, 0, 1, 0, 0));
        casas.add(new Casa("RUA D", 150, Color.GREEN, "TERRENO", 5, 450, 0, 1, 20, 100));
        casas.add(new Casa("RUA E", 200, Color.GREEN, "TERRENO", 6, 525, 0, 1, 30, 120));
        casas.add(new Casa("RUA F", 250, Color.GREEN, "TERRENO", 7, 600, 0, 1, 40, 140));
        casas.add(new Casa("S / R", 0, Color.BLACK, "SOR", 8, 675, 0, 1, 0, 0));
        casas.add(new Casa("PRISÃO / VISITAS", 0, Color.BLACK, "VISITAS", 9, 750, 0, 1, 0, 0));
        // casas direita - 8
        casas.add(new Casa("RUA G", 300, Color.YELLOW, "TERRENO", 10, 750, 150, 0, 50, 120));
        casas.add(new Casa("RUA H", 350, Color.YELLOW, "TERRENO", 11, 750, 225, 0, 60, 140));
        casas.add(new Casa("DOAÇÃO", 400, Color.BLACK, "DOACAO", 12, 750, 300, 0, 0, 0));
        casas.add(new Casa("RUA I", 500, Color.BLUE, "TERRENO", 13, 750, 375, 0, 80, 200));
        casas.add(new Casa("RUA J", 600, Color.BLUE, "TERRENO", 14, 750, 450, 0, 100, 220));
        casas.add(new Casa("RUA K", 700, Color.BLUE, "TERRENO", 15, 750, 525, 0, 120, 250));
        // casas base - 10
        casas.add(new Casa("FÉRIAS", 0, Color.BLACK, "FERIAS", 16, 750, 600, 1, 0, 0));
        casas.add(new Casa("RUA L", 350, Color.MAGENTA, "TERRENO", 17, 675, 600, 1, 60, 150));
        casas.add(new Casa("RUA M", 400, Color.MAGENTA, "TERRENO", 18, 600, 600, 1, 70, 170));
        casas.add(new Casa("RUA N", 450, Color.MAGENTA, "TERRENO", 19, 525, 600, 1, 80, 200));
        casas.add(new Casa("MULTA", 400, Color.BLACK, "MULTA", 20, 450, 600, 1, 0, 0));
        casas.add(new Casa("RUA O", 100, Color.PINK, "TERRENO", 21, 375, 600, 1, 15, 45));
        casas.add(new Casa("RUA P", 150, Color.PINK, "TERRENO", 22, 300, 600, 1, 25, 60));
        casas.add(new Casa("RUA Q", 200, Color.PINK, "TERRENO", 23, 225, 600, 1, 35, 90));
        casas.add(new Casa("S / R", 0, Color.BLACK, "SOR", 24, 150, 600, 1, 0, 0));
        casas.add(new Casa("CAMBURÃO", 0, Color.BLACK, "PRISAO", 25, 0, 600, 1, 0, 0));
        // casas esquerda - 8
        casas.add(new Casa("RUA R", 80, Color.CYAN, "TERRENO", 26, 0, 525, 0, 10, 20));
        casas.add(new Casa("RUA S", 100, Color.CYAN, "TERRENO", 27, 0, 450, 0, 15, 30));
        casas.add(new Casa("DOAÇÃO", 450, Color.BLACK, "DOACAO", 28, 0, 375, 0, 0, 0));
        casas.add(new Casa("S / R", 0, Color.BLACK, "SOR", 29, 0, 300, 0, 0, 0));
        casas.add(new Casa("RUA T", 500, Color.ORANGE, "TERRENO", 30, 0, 225, 0, 100, 200));
        casas.add(new Casa("RUA U", 400, Color.ORANGE, "TERRENO", 31, 0, 150, 0, 80, 150));


    }

    // metodo para iniciar jogadores
    private void inicializarJogadores(){
        jogadores.add(new Jogador(0, "Jogador VERMELHO", Color.RED));
        jogadores.add(new Jogador(1, "Jogador AZUL", Color.BLUE));
        jogadores.add(new Jogador(2, "Jogador VERDE", Color.GREEN));
        jogadores.add(new Jogador(3, "Jogador AMARELO", Color.YELLOW));
        jogadores.add(new Jogador(4, "Jogador CIANO", Color.CYAN));
        jogadores.add(new Jogador(5, "Jogador MAGENTA", Color.MAGENTA));
    }

    // metodo para atualizar a posicao dos jogadores
    public void atualizarJogadores(int posicaoAnterior, int novaPosicao, Jogador jogador) {
        // remove o jogador da casa antiga
        if (posicaoAnterior >= 0) {
            casas.get(posicaoAnterior).removerJogador(jogador);
        }
        // adiciona o jogador a nova casa
        casas.get(novaPosicao).adicionarJogador(jogador);
    }

    // metodo para iniciar as cartas de Sorte ou Reves, cada uma com suas caracteristicas especificas
    public void inicializarCartas(){
        cartas.add(new SorteOuReves("Você encontrou uma carteira na rua, receba $200", 200));
        cartas.add(new SorteOuReves("Você pagou uma multa por estacionamento irregular, perca $100", -100));
        cartas.add(new SorteOuReves("Você ganhou na loteria! Receba $1.000", 1000));
        cartas.add(new SorteOuReves("Seu carro quebrou, pague $300 para o conserto", -300));
        cartas.add(new SorteOuReves("Você recebeu uma herança inesperada, receba $700", 700));
        cartas.add(new SorteOuReves("Você doou dinheiro para uma instituição de caridade, perca $150", -150));
        cartas.add(new SorteOuReves("Você foi promovido no trabalho! Receba $500", 500));
        cartas.add(new SorteOuReves("Você caiu em um golpe online e perdeu $400", -400));
        cartas.add(new SorteOuReves("Você ganhou um prêmio em um concurso, receba $300", 300));
        cartas.add(new SorteOuReves("Você esqueceu de pagar uma conta, perca $200", -200));
        cartas.add(new SorteOuReves("Você foi pego em uma blitz e vai para a prisão", 0));
        cartas.add(new SorteOuReves("Você recebeu um bônus de final de ano, receba $600", 600));
        cartas.add(new SorteOuReves("Sua empresa sofreu prejuízo, perca $250", -250));
        cartas.add(new SorteOuReves("Você encontrou um item raro e o vendeu, receba $400", 400));
        cartas.add(new SorteOuReves("Você organizou uma festa cara, perca $350", -350));
        cartas.add(new SorteOuReves("Você recebeu um reembolso de imposto, receba $450", 450));
        cartas.add(new SorteOuReves("Você foi chamado para o início do tabuleiro!", 0));
        cartas.add(new SorteOuReves("Você tirou férias e gastou além do orçamento, perca $500", -500));
        cartas.add(new SorteOuReves("Você participou de uma promoção e ganhou $250", 250));
        cartas.add(new SorteOuReves("Você perdeu dinheiro em um investimento, perca $600", -600));
    }

    // metodod para quando um jogador falir
    public void decretarFalencia(Jogador jogador){
        JOptionPane.showMessageDialog(null, "Voce FALIU! GAME OVER");
        this.getJogadores().remove(jogador);
    }

    // Getters e Setters

    public List<Casa> getCasas() {
        return casas;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public List<SorteOuReves> getCartas(){
        return cartas;
    }
}