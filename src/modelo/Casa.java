package modelo;

import java.awt.Color;
import java.awt.*;
import javax.swing.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



public class Casa implements Serializable {
    private String nome;                            // nome da casa
    private int valor;                              // valor de compra ou debitar/creditar(caso DOACAO ou MULTA)
    private Color cor;                              // cor correspondente
    private String tipo;                            // tipo
    private Jogador dono;                           // dono da casa
    private int indice;                             // indice da casa
    private JPanel P;                               // Panel para desenhar os objetos
    private JLabel textoT;                          // texto superior
    private JLabel textoB;                          // texto inferior
    private JPanel panelJogadores;                  // Panel para desenhar os jogadores
    private Map<Jogador, JPanel> mapaJogadores;     // hashmap para desenhar os jogadores na casa
    private int aluguel;                            // valor do aluguel atual
    private int numCasas;                           // numero de casas construidas na propriedade
    private int maxCasas;                           // maximo de casas que podem ser construidas = 4
    private int aluguelIni;                         // valor do aluguel inicial
    private int hipoteca;                           // valor de hipoteca
    private int valorConstruir;

    // Construtor
    public Casa(String nome, int valor, Color cor, String tipo, int indice, int x, int y, int orientacao, int aluguel, int hipoteca) {
        // inicializacao dos atributos
        this.nome = nome;
        this.valor = valor;
        this.cor = cor;
        this.tipo = tipo;
        this.indice = indice;
        this.aluguel = aluguel;
        this.aluguelIni = aluguel;
        this.hipoteca = hipoteca;
        this.dono = null;
        this.numCasas = 0;
        this.maxCasas = 4;
        this.valorConstruir = 300;
        this.mapaJogadores = new HashMap<>();

        // inicializacao do panel da casa
        this.P = new JPanel();
        this.P.setBackground(Color.WHITE);
        this.P.setBorder(BorderFactory.createLineBorder(cor, 5));
        this.P.setLayout(new BorderLayout());

        // inicializacao do texto superior, que indica o nome da casa
        this.textoT = new JLabel(nome, SwingConstants.CENTER);
        this.textoT.setVerticalAlignment(SwingConstants.CENTER);
        this.textoT.setHorizontalAlignment(SwingConstants.CENTER);

        // as pontas possuem tamanho 15x15 e as "arestas" 150x75 ou 75x150, depende da orientacao
        if(indice == 0 || indice == 9 || indice == 16 || indice == 25) {
            this.P.setBounds(x, y, 150, 150);
        }
        else {

            if (orientacao == 1) // 1 se horizontal
                this.P.setBounds(x, y, 75, 150);
            else
                this.P.setBounds(x, y, 150, 75);
        }

        this.P.add(textoT, BorderLayout.NORTH);

        // texto inferior referente ao valor de compra
        this.textoB = new JLabel("$"+valor, SwingConstants.CENTER);
        if(valor != 0)
            this.P.add(textoB, BorderLayout.SOUTH);

        // panel separado para agrupar os jogadores e construcoes
        this.panelJogadores = new JPanel();
        this.panelJogadores.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        this.panelJogadores.setOpaque(false);
        this.P.add(panelJogadores, BorderLayout.CENTER);
    }

    // metodo para adicionar jogador na casa
    public void adicionarJogador(Jogador jogador){
        // cria novo panel para colocar na casa
        JPanel jogadorPanel = new JPanel();
        jogadorPanel.setBackground(jogador.getCor());
        jogadorPanel.setPreferredSize(new Dimension(15, 15));
        jogadorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // adiciona na casa e vincula esse panel ao jogador
        this.panelJogadores.add(jogadorPanel);
        this.mapaJogadores.put(jogador, jogadorPanel);

        this.panelJogadores.add(jogadorPanel);
        this.panelJogadores.revalidate();
        this.panelJogadores.repaint();
    }

    // metodo para remover jogador da casa
    public void removerJogador(Jogador jogador){
        // remove jogador da casa
        JPanel jogadorPanel = this.mapaJogadores.get(jogador);
        if (jogadorPanel != null) {
            this.panelJogadores.remove(jogadorPanel);
            this.mapaJogadores.remove(jogador);

            this.panelJogadores.revalidate();
            this.panelJogadores.repaint();
        }
    }

    // metodo para construir casa da cor do jogador
    public void constroiCasa(Jogador jogador){
        // cria novo panel para casa
        JPanel casaPanel = new JPanel();
        casaPanel.setBackground(jogador.getCor());
        casaPanel.setPreferredSize(new Dimension(5, 10));

        // incrementa o numero de casas
        this.numCasas++;
        // dobra o aluguel a cada casa contruida
        this.aluguel = this.aluguel * 2;

        // adiciona o novo panel
        this.panelJogadores.add(casaPanel, BorderLayout.CENTER);

        this.panelJogadores.revalidate();
        this.panelJogadores.repaint();
    }

    // metodo para remover todas as casa, usado quando um jogador tem q hipotecar a propriedade
    public void removerTodasCasas(Jogador jogador){
        // vai buscando os componentes e removendo
        Component[] components = this.panelJogadores.getComponents();
        for(Component component : components){
            if(component instanceof JPanel && component.getBackground().equals(jogador.getCor())){
                this.panelJogadores.remove(component);
            }
        }

        // zera o numero de casas
        this.numCasas = 0;
        // retorna o aluguel inicial
        this.aluguel = this.aluguelIni;

        this.panelJogadores.revalidate();
        this.panelJogadores.repaint();
    }

    // Getters e Setters

    public String getNome(){
        return this.nome;
    }

    public int getValor(){
        return this.valor;
    }

    public Color getCor(){
        return this.cor;
    }

    public String getTipo(){
        return this.tipo;
    }

    public Jogador getDono(){
        return this.dono;
    }

    public void setDono(Jogador dono){
        this.dono = dono;
    }

    public JPanel getP(){
        return this.P;
    }

    public int getAluguel() {
        return this.aluguel;
    }

    public void setAluguel(int aluguel){
        this.aluguel = aluguel;
    }

    public int getNumCasas(){
        return this.numCasas;
    }

    public int getMaxCasas(){
        return this.maxCasas;
    }

    public int getHipoteca(){
        return this.hipoteca;
    }

    public int getAluguelIni(){
        return  this.aluguelIni;
    }

    public int getValorConstruir(){
        return this.valorConstruir;
    }
}