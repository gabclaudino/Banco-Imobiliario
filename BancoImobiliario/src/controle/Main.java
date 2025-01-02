package controle;

import visao.Tela;

import java.awt.event.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame janela = Tela.getInstancia();
        janela.setVisible(true);
        WindowListener x = new WindowAdapter ()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        };
        janela.addWindowListener(x);
    }
}
