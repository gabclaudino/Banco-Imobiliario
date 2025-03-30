package visao;

import java.awt.event.*;
import javax.swing.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import modelo.SorteOuReves;
import modelo.Tabuleiro;
import modelo.Jogador;
import modelo.Casa;


public class Tela extends JFrame implements ActionListener {

    private static Tela instancia;          // instancia unica do padrao Singleton

    private JButton dado;                   // botao para rodar os dados
    private JButton salvar;                 // botao para salvar o estado da partida
    private JButton carregar;               // botao para carregar o estado de uma partida previamente salva

    private JLabel vezJogador;              // label para informar quem esta na vez de jogar
    private JLabel saldoJogador;            // label para informar o saldo do jogador da vez
    private JLabel propriedadesJogador;     // label para informar as propriedades do jogador da vez

    private int jogadorAtual;               // var para salvar qual jogador esta na vez de jogar

    // cria o tabuleiro
    Tabuleiro tabuleiro = Tabuleiro.getInstancia();

    // construtor
    public Tela() {
        // chama o metodo de iniciar a interface
        iniciaInterface();
    }

    // padrao Singleton
    public static synchronized Tela getInstancia(){
        if (instancia == null) {
            instancia = new Tela();
        }
        return instancia;
    }

    // metodo para iniciar a interface
    public void iniciaInterface(){

        // define o tamanho da janela
        this.getContentPane().setLayout(null);
        this.setSize(1500,789);

        // adiciona os jogadores no tabuleiro
        for(Jogador jogador : tabuleiro.getJogadores()) {
            tabuleiro.getCasas().get(0).adicionarJogador(jogador);
        }

        // marca o primeiro jogador para comecar a jogada
        this.jogadorAtual = 0;



        // laco usado para teste onde o primeiro jogador possui todas as propriedades. usado para verificar a contrucao das casas
        /*for (Casa casaAtual : tabuleiro.getCasas()){
            if(casaAtual.getTipo().equalsIgnoreCase("TERRENO")) {
                casaAtual.setDono(tabuleiro.getJogadores().getFirst());
                tabuleiro.getJogadores().getFirst().adicionarPropriedade(casaAtual);
            }
        }*/



        //laco usado para teste onde o jogardor VERMELHO possui todas as casas ate a primeira metade do tabuleiro e o jogador VERDE todas da segunda metade
        /*
        for (int i = 0; i < tabuleiro.getCasas().size()/2; i++)
        {
            if(tabuleiro.getCasas().get(i).getTipo().equalsIgnoreCase("TERRENO")){
                tabuleiro.getCasas().get(i).setDono(tabuleiro.getJogadores().get(0));
                tabuleiro.getJogadores().get(0).adicionarPropriedade(tabuleiro.getCasas().get(i));
            }
        }

        for (int i = tabuleiro.getCasas().size()/2 + 1; i < tabuleiro.getCasas().size(); i++)
        {
            if(tabuleiro.getCasas().get(i).getTipo().equalsIgnoreCase("TERRENO")){
                tabuleiro.getCasas().get(i).setDono(tabuleiro.getJogadores().get(2));
                tabuleiro.getJogadores().get(2).adicionarPropriedade(tabuleiro.getCasas().get(i));
            }
        }
        */


        // metododo para iniciar os itens da interface
        inicializaItens();

        // atualizar o desenho da interface
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }

    // metodo para atualizar a interace
    // aqui que acontece o laco principal do jogo
    public void atualizaInterface(){
        // pega o jogador da vez e atribui em "jogador"
        Jogador jogador = tabuleiro.getJogadores().get(jogadorAtual);

        // verifica se o jogador nao esta preso
        if(jogador.getPreso()){
            // se esta, tenta sair da prisao
            jogador.tentarSairDaPrisao();

            // passa a vez para o proximo jogador
            itera();
            return;
        }

        // verifica se o jogador esta de ferias ou nao
        else if(jogador.getFerias())
        {
            // se esta, o jogador nao joga esta rodada
            JOptionPane.showMessageDialog(this, "As ferias acabaram! Na proxima rodada voce podera jogar!");
            jogador.setFerias(false);

            // passa a vez para o proximo
            itera();
            return;

        } else{ // se nao esta nem preso nem de ferias, entao faz a jogada normal
            // rolar dos dados
            int dado1 = (int) (Math.random() * 6 + 1);
            int dado2 = (int) (Math.random() * 6 + 1);
            JOptionPane.showMessageDialog(this, "Voce rolou " + dado1 + " e " + dado2 + " nos dados!");

            // move o jogador para a posicao nova (soma dos dados)
            jogador.mover(dado1 + dado2, tabuleiro);

            // salva a casa em que o jogador caiu
            Casa casaAtual = tabuleiro.getCasas().get(jogador.getPosicao());

            // switchcase para o tipo da casa que o jogador caiu
            switch (casaAtual.getTipo()) {
                // tipo TERRENO(propriedade)
                case "TERRENO":
                    // veirifca se a propriedade ja tem dono
                    if (casaAtual.getDono() == null) {
                        // se nao, pergunta se deseja compra-la
                        int resposta = JOptionPane.showConfirmDialog(this, "Deseja comprar a propriedade " + casaAtual.getNome() + " por $" + casaAtual.getValor() + "? Aluguel: $" + casaAtual.getAluguel(),
                                "Comprar Propriedade", JOptionPane.YES_NO_OPTION);
                        if (resposta == JOptionPane.YES_OPTION) {
                            // verifica se o jogador tem saldo o suficiente para comprar
                            if (jogador.getSaldo() >= casaAtual.getValor()) {
                                // se sim, entao tira o valor de compra da casa de seu saldo
                                jogador.setSaldo(jogador.getSaldo() - casaAtual.getValor());
                                // marca o dono da casa como o jogador atual
                                casaAtual.setDono(jogador);
                                // adiciona a propriedade a lista d epropriedades do jogador
                                jogador.adicionarPropriedade(casaAtual);
                                JOptionPane.showMessageDialog(this, "Voce comprou " + casaAtual.getNome() + "!");
                            } else {
                                // se nao tem dinheiro, entao nao compra
                                JOptionPane.showMessageDialog(this, "Voce nao tem dinheiro para comprar a propriedade!");
                            }
                        }
                        // se a propriedade ja tem dono, entao ou eh o proprio jogador da vez ou outro
                    } else {
                        // verifica se o dono da casa nao eh o proprio jogador
                        if (casaAtual.getDono().getIndice() == jogador.getIndice()) {
                            JOptionPane.showMessageDialog(this, "Voce ja eh dono desta propriedade");
                            // verifica se o jogador ja possiu todas as prorpiedades daquela cor
                            if (jogador.possuiTodasCores(casaAtual.getCor(), tabuleiro)){
                                // verifica se o ja foi atingido o numero maximo de casa construidas na propriedade
                                if(casaAtual.getNumCasas() < casaAtual.getMaxCasas()) {
                                    // se nao, pergunta ao jogador se deseja construir
                                    int resposta = JOptionPane.showConfirmDialog(this, "Voce possui todas as propriedades dessa cor! Gostaria de construir uma casa por $300?","Comprar casa", JOptionPane.YES_NO_OPTION);
                                    if(resposta == JOptionPane.YES_OPTION) {
                                        // se sim, entao verifica se o jogador tem o valor necessario para construir uma casa (fixo $300)
                                        if(jogador.getSaldo() < casaAtual.getValorConstruir()) {
                                            // se nao tem, nao constroi
                                            JOptionPane.showMessageDialog(this, "Voce nao tem dinheiro o suficiente para construir uma casa!");
                                        }
                                        else {
                                            // se sim, entao constroi a casa e retira do saldo do jogador os $300
                                            jogador.setSaldo(jogador.getSaldo() - casaAtual.getValorConstruir());
                                            casaAtual.constroiCasa(jogador);
                                        }
                                    }
                                }
                            }
                        } else { // se nao eh o proprio jogador o proprietario, entao deve pagar aluguel
                            JOptionPane.showMessageDialog(this, "Esta casa percente a " + casaAtual.getDono().getNome() + "! Pague $" + casaAtual.getAluguel() + " de aluguel.");
                            // verifica se o jogador tem saldo o suficiente para pagar o aluguel
                            if (jogador.getSaldo() >= casaAtual.getAluguel()) {
                                // se sim entao retira do jogador atual e passa para o dono da propriedade
                                jogador.setSaldo(jogador.getSaldo() - casaAtual.getAluguel());
                                casaAtual.getDono().setSaldo(casaAtual.getDono().getSaldo() + casaAtual.getAluguel());
                            } else {
                                // se nao, o jogador eh obrigado a hipotecar sua propriedade de maior valor de hipoteca
                                JOptionPane.showMessageDialog(this, "Voce nao tem dinheiro o suficiente!");
                                jogador.hipotecarPropriedadeMaisCara();
                                // vai hipotecando ate conseguir pagar o aluguel
                                while((jogador.getSaldo() < casaAtual.getAluguel() && !jogador.getPropriedades().isEmpty())){
                                    JOptionPane.showMessageDialog(this, "Voce ainda nao tem dinheiro o suficiente! Devera hipotecar mais propriedades!");
                                    jogador.hipotecarPropriedadeMaisCara();
                                }
                                // verifica se o jogador ou hipotecou tudo e nao consegiu pagar, ou hipotecou o suficiente par apagar o aluguel
                                if(!jogador.getPropriedades().isEmpty()){
                                    jogador.setSaldo(jogador.getSaldo() - casaAtual.getAluguel());
                                    casaAtual.getDono().setSaldo(casaAtual.getDono().getSaldo() + casaAtual.getAluguel());
                                }
                                // se hipotecou tudo e nao consegiu pagar, entao o jogador perdeu
                                else{
                                    // decretar falencia
                                    tabuleiro.decretarFalencia(jogador);
                                }

                            }
                        }
                    }
                    break;

                    // tipo MULTA, basicamente serve para retirar dinehiro do jogador
                case "MULTA":
                    JOptionPane.showMessageDialog(this, "Voce foi multado de devera pagar $" + casaAtual.getValor() + "!");
                    // verifica se o jogaor tem saldo o suficiente para pagar a multa
                    if (jogador.getSaldo() >= casaAtual.getValor()) {
                        // se sim, eh retirado o valor da multa do saldo do jogador
                        jogador.setSaldo(jogador.getSaldo() - casaAtual.getValor());
                    } else {
                        // se nao possui, vai ter que hipotecar as propriedades
                        JOptionPane.showMessageDialog(this, "Voce nao possui dinheiro suficiente!");
                        jogador.hipotecarPropriedadeMaisCara();
                        // vai hipotecando ate poder pagar ou acabar as propriedades
                        while((jogador.getSaldo() < casaAtual.getValor() && !jogador.getPropriedades().isEmpty())){
                            JOptionPane.showMessageDialog(this, "Voce ainda nao tem dinheiro o suficiente! Devera hipotecar mais propriedades!");
                            jogador.hipotecarPropriedadeMaisCara();
                        }
                        // verifica se o jogador conseguiu pagar, ou faliu
                        if(!jogador.getPropriedades().isEmpty()) {
                            jogador.setSaldo(jogador.getSaldo() - casaAtual.getValor());
                        }
                        else {
                            // decretar falencia
                            tabuleiro.decretarFalencia(jogador);
                        }
                    }
                    break;

                    // tipo Inicio, se o jogador cair na casa inicio, entao recebe $500
                case "INICIO":
                    JOptionPane.showMessageDialog(this, "Voce deu uma volta completa! Receba $500.");
                    jogador.setSaldo(jogador.getSaldo() + 500);
                    break;

                    // tipo VISITAS, nao acontece nada
                case "VISITAS":
                    JOptionPane.showMessageDialog(this, "Voce foi visitar um parente na prisao");
                    break;

                    // tipo DOACAO, o jogador recebe uma certa quantia
                case "DOACAO":
                    JOptionPane.showMessageDialog(this, "voce recebeu uma doacao inesperada! Receba $" + casaAtual.getValor());
                    jogador.setSaldo(jogador.getSaldo() + casaAtual.getValor());
                    break;

                    // tipo FERIAS, o jogador fica uma rodade sem poder jogar
                case "FERIAS":
                    JOptionPane.showMessageDialog(this, "voce decidiu entrar de Ferias! Fique uma rodada sem jogar.");
                    // marca que o jogador esta de ferias
                    jogador.setFerias(true);
                    break;

                    // tipo PRISAO, o jogador vai preso e fica 3 rodadas sem poder jogar
                case "PRISAO":
                    JOptionPane.showMessageDialog(this, "Voce foi preso! Fique 3 rodadas sem jogar!");
                    // marca o jogador como preso
                    jogador.setPreso(true);
                    // inicia o contador de tempo preso
                    jogador.setRodadasPreso(3);
                    // move o jogador para a casa da prisao
                    jogador.mover(16, tabuleiro);
                    break;

                    // tipo SORTE OU REVES, o jogador tira uma carta escolhida aleatoriamente, podendo ser SORTE ou REVES
                case "SOR":
                    // sorteia um numero aleatorio
                    int num = (int) (Math.random() * tabuleiro.getCartas().size());

                    // carta = carta do indice(numero gerado aleatoriamente) do da lista
                    SorteOuReves carta = tabuleiro.getCartas().get(num);

                    // mostra a carta para o jogador
                    JOptionPane.showMessageDialog(this, carta.getTexto());

                    // se for REVES verifica se o jogador tem saldo o suficiente para pagar
                    if (carta.getValor() < 0) {
                        if (jogador.getSaldo() < Math.abs(carta.getValor())) {
                            // se nao tem entao devera hipotecar a propriedade mais cara
                            JOptionPane.showMessageDialog(this, "Voce nao tem dinheiro o suficiente para pagar!");
                            jogador.hipotecarPropriedadeMaisCara();
                            // vai hipotecando ate conseguir pagar, ou falir
                            while((jogador.getSaldo() < carta.getValor() && !jogador.getPropriedades().isEmpty())){
                                JOptionPane.showMessageDialog(this, "Voce ainda nao tem dinheiro o suficiente! Devera hipotecar mais propriedades!");
                                jogador.hipotecarPropriedadeMaisCara();
                            }
                            if(!jogador.getPropriedades().isEmpty()) {
                                jogador.setSaldo(jogador.getSaldo() - carta.getValor());
                            }
                            // se acabou as propriedades e nao conseguiu pagar, entao faliu
                            else{
                                // decretar falencia
                                tabuleiro.decretarFalencia(jogador);
                            }

                        // se o jogador tem saldo, entao paga
                        } else {
                            // nesse caso o valor vai ser negativo, entao eh a operacao de soma mesmo
                            jogador.setSaldo(jogador.getSaldo() + carta.getValor());
                        }
                        // se for SORTE, entao eh adicionado o valor ao saldo
                    } else {
                        jogador.setSaldo(jogador.getSaldo() + carta.getValor());
                    }
                    break;
                default:
                    break;
            }
        }

        // passa a vez para o proximo e faz as atualizacoes na interface
        itera();
    }

    // metodo para formatar a impressao das propriedaes do jogador
    private String formatarPropriedades(Jogador jogador) {
        StringBuilder propriedades = new StringBuilder();
        // vai salvando o nome das propriedades e seu valor de aluguel, cada consjunto separado por uma quebra de linha
        for (Casa casa : jogador.getPropriedades()) {
            propriedades.append(casa.getNome())
                    .append(" - Aluguel $")
                    .append(casa.getAluguel())
                    .append("<br>");
        }

        // retorna a string com todas as propriedades e seus valores de aluguel
        return propriedades.toString();
    }

    // metodo para passar a vez para o proximo jogador e atualizar os dados da tela
    public void itera(){
        // verifica se ha apenas um jogador na partida
        // se sim, significa que os outros faliram, entao o que restou eh o campeao
        if(tabuleiro.getJogadores().size() == 1) {
            JOptionPane.showMessageDialog(this, "O jogo acabou e o vencedor foi "+tabuleiro.getJogadores().get(0)+"! Parabéns!");
            System.exit(0);
        }

        // passa a vez para o proximo jogador
        jogadorAtual = (jogadorAtual + 1) % tabuleiro.getJogadores().size();
        // atualiza os daos da inerface para os dados do proximo jogador
        vezJogador.setText("Vez do jogador: "+tabuleiro.getJogadores().get(jogadorAtual).getNome());
        saldoJogador.setText("Saldo: $" + tabuleiro.getJogadores().get(jogadorAtual).getSaldo());
        propriedadesJogador.setText("<html>Propriedades:<br>" + formatarPropriedades(tabuleiro.getJogadores().get(jogadorAtual)) + "</html>");
    }

    // metodo para salvar o estado do jogo em um arquivo chamado "save.ser"
    public void salvaJogo(){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.ser"))){
            out.writeObject(this);
            JOptionPane.showMessageDialog(this,"Jogo salvo com sucesso!");

        } catch (IOException e){
            JOptionPane.showMessageDialog(this,"Erro ao salvar o jogo!" + e.getMessage());
        }
    }

    // metodo para carregar o estado de um jogo salvo, a partir de um arquivo "save.ser"
    public void carregaJogo(){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("save.ser"))){
            Tela telaCarregada = (Tela) in.readObject();
            // carrega as informacoes dos objetos dos arquivos nos objetos atuais
            this.tabuleiro = telaCarregada.tabuleiro;
            this.jogadorAtual = telaCarregada.jogadorAtual;

            // atualiza o tabuleiro
            atualizaTabuleiro();

            // mensagem de confirmacao de jogo carregado
            JOptionPane.showMessageDialog(this, "Jogo carregado com sucesso!");
        } catch (IOException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(this,"Erro ao carregar o save "+ e.getMessage());
        }
    }

    // metodo para atualizar o tabuleiro apos ser carregado um save
    public void atualizaTabuleiro(){
        // tira todos os componentes da interface
        this.getContentPane().removeAll();

        // chama o metodo para inicilizar os componentes novamente, porem os novos que foram carregados
        inicializaItens();

        // atualiza a impressao da interface
        this.getContentPane().revalidate();
        this.getContentPane().repaint();

    }

    // metodo para inicar os itens da interface
    public void inicializaItens(){

        // adiciona os panels das casas
        for(Casa casa : tabuleiro.getCasas()) {
            this.getContentPane().add(casa.getP());
        }

        // adiciona o label de texto para informar qual o jogador esta na vez
        vezJogador = new JLabel("Vez do jogador: " + tabuleiro.getJogadores().get(jogadorAtual).getNome());
        vezJogador.setBounds(900, 20, 300, 20);
        vezJogador.setHorizontalAlignment(SwingConstants.CENTER);
        this.getContentPane().add(vezJogador);

        // adiciona o label de texto para informar o saldo do jogador atual
        saldoJogador = new JLabel("Saldo: $" + tabuleiro.getJogadores().get(jogadorAtual).getSaldo());
        saldoJogador.setBounds(1155, 160, 200, 20);
        saldoJogador.setHorizontalAlignment(SwingConstants.CENTER);
        this.getContentPane().add(saldoJogador);

        // adiciona o label de texto para informar quais propriedades o jogador possui
        propriedadesJogador = new JLabel("<html>Propriedades: " + formatarPropriedades(tabuleiro.getJogadores().get(jogadorAtual)) + "</html>");
        propriedadesJogador.setBounds(1155, 180, 200, 500);
        propriedadesJogador.setVerticalAlignment(SwingConstants.TOP);
        propriedadesJogador.setHorizontalAlignment(SwingConstants.CENTER);
        this.getContentPane().add(propriedadesJogador);

        // adiciona o botao de rolar os dados
        dado = new JButton("Rolar Dados");
        dado.addActionListener(this);
        dado.setBounds(1200, 50, 110, 110);
        this.getContentPane().add(dado);

        // adiciona o botao de salvar o jogo
        salvar = new JButton("Salvar jogo");
        salvar.addActionListener(this);
        salvar.setBounds(1350, 670, 100, 50);
        this.getContentPane().add(salvar);

        // adiciona o botao de carregar jogo
        carregar = new JButton("Carregar jogo");
        carregar.addActionListener(this);
        carregar.setBounds(1200, 670, 120, 50);
        this.getContentPane().add(carregar);
    }

    // metodo de eventos
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dado) {
            atualizaInterface();
        }
        if (e.getSource() == salvar){
            salvaJogo();
        }
        if (e.getSource() == carregar){
            carregaJogo();
        }
    }

}
