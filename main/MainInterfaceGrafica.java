package main;

import javax.swing.*;
import main.Control.PosicaoReal;
import java.awt.*;
import java.util.ArrayList;
import main.Control.Jogada;
/**
 * @author Matheus
 */
public final class MainInterfaceGrafica extends JFrame {

    private final int TAMANHO = 6;
    private final CasaBotao[][] tabuleiroInterface = new CasaBotao[TAMANHO][TAMANHO];
    Control controle = new Control();
    private final Tabuleiro tabuleiroLogico;
    private char caracterCasa = 'A'; 
    private int linhaOrigem = -1, colOrigem = -1;
    private int profundidade = 8;
    private boolean corIA;

    public MainInterfaceGrafica() {
        
        /*
            TABULEIRO DO JOGO
        */
        tabuleiroLogico = new Tabuleiro(controle);

        setTitle("DISCIPLINA - IA - MINI JOGO DE DAMA");
        setSize(500, 500);
        setLayout(new GridLayout(TAMANHO, TAMANHO));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
        sincronizarInterface(); 

        setVisible(true);
    }

    private void setCorIA(){

        int aleatorio = (int)(Math.random() * 2);

        if(aleatorio == 0){

            corIA = true;
        }
        else{

            corIA = false;
        }
    }

    private void inicializarComponentes() {

        setCorIA();
        System.out.println("Cor da IA: " + corIA); //true se branca, false se preta
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiroInterface[i][j] = new CasaBotao();

                // Cores do tabuleiro
                if ((i + j) % 2 == 0) {
                    tabuleiroInterface[i][j].setBackground(new Color(255, 255, 255)); // Bege
                } else {
                    controle.setarCasas(caracterCasa, i, j);
                    controle.setarChaves(i, j, caracterCasa);
                    caracterCasa++;
                    tabuleiroInterface[i][j].setBackground(new Color(100, 100, 100));  // Verde
                }

                int linha = i;
                int coluna = j;
                tabuleiroInterface[i][j].addActionListener(e -> tratarClique(linha, coluna));
                add(tabuleiroInterface[i][j]);
            }
        }
        
        if(corIA)   //IA tem a primeira jogada
            fazerJogadaIA();
        
    }

    private void tratarClique(int linha, int col) {
        

        // Caso 1: Nenhuma peça selecionada ainda
        if (linhaOrigem == -1) {
            

            // Verifica se a casa clicada contém QUALQUER peça (1, 2, 3 ou 4)
            if (tabuleiroLogico.getMatriz()[linha][col] != '0') {
                
                linhaOrigem = linha;
                colOrigem = col;
                tabuleiroInterface[linha][col].setBackground(Color.YELLOW); // Destaque do clique
            }
        } 
        // Caso 2: Já existe uma peça selecionada, tentando mover
        else {
            
            if (linhaOrigem == linha && colOrigem == col) {
                cancelarSelecao(linhaOrigem, colOrigem);
                return;
            }

            boolean sucesso = moverPecaLogica(linhaOrigem, colOrigem, linha, col);

            if (sucesso) {
                cancelarSelecao(linhaOrigem, colOrigem);
                sincronizarInterface();
                
                if(!tabuleiroLogico.isLocked()){

                    ArrayList<Jogada> jogadas = tabuleiroLogico.getMovimentosPossiveis(tabuleiroLogico, controle.turnoBranca);

                    if (jogadas.size() > 0)
                        fazerJogadaIA();
                    else{
                        JOptionPane.showMessageDialog(this, "Fim de jogo! Vitória das brancas "+ "!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    }

                }
            } else {
                cancelarSelecao(linhaOrigem, colOrigem);
            }
        }


    }

    private void cancelarSelecao(int lin, int col) {
        if (linhaOrigem != -1) {
            // Restaura a cor original
                if ((lin + col) % 2 == 0) {
                    tabuleiroInterface[lin][col].setBackground(new Color(255, 255, 255)); // Bege
                } else {
                    tabuleiroInterface[lin][col].setBackground(new Color(100, 100, 100));  // Verde
                }
        }
        linhaOrigem = -1;
        colOrigem = -1;
        
    }

    private boolean moverPecaLogica(int r1, int c1, int r2, int c2) {

        // A casa de destino deve estar vazia
        if (tabuleiroLogico.getMatriz()[r2][c2] == '0') {
            
            if(!controle.getTurno(tabuleiroLogico.getMatriz()[r1][c1]))
                return false;

            if (!tabuleiroLogico.verificaMovimento(r1, c1, r2, c2))          
                return false;

            //se o tabuleiro não tem uma peça para jogada encadeada, muda o turno
            if (!tabuleiroLogico.isLocked())
                controle.mudarJogada();
            // Transfere o valor (seja 1, 2, 3 ou 4) para a nova posição
            
            tabuleiroLogico.getMatriz()[r2][c2] = tabuleiroLogico.getMatriz()[r1][c1];
            tabuleiroLogico.getMatriz()[r1][c1] = '0';

            // Promoção simples para Dama (opcional)
            if (tabuleiroLogico.getMatriz()[r2][c2] == '2' && r2 == 5) {
                controle.damasPretas++;
                tabuleiroLogico.getMatriz()[r2][c2] = '4';
            }
            if (tabuleiroLogico.getMatriz()[r2][c2] == '1' && r2 == 0) {
                controle.damasBrancas++;
                tabuleiroLogico.getMatriz()[r2][c2] = '3';
            }

            if(tabuleiroLogico.verificarFimPartida()){

                    int vencedora = this.controle.pecasBrancas - this.controle.pecasPretas;
                    vencedora = (vencedora < 0) ? 0 : 1;
                    // se for < 0, pretas ganharam
                    if(vencedora == 0)
                        JOptionPane.showMessageDialog(this, "Fim de jogo! Vitória das pretas "+ "!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    
                    else
                        JOptionPane.showMessageDialog(this, "Fim de jogo! Vitória das brancas "+ "!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    
                    this.dispose();
                }

            return true;
        }
        return false;
    }

    private void fazerJogadaIA(){

        boolean vezIA = (controle.turnoBranca && corIA) || (!controle.turnoBranca && !corIA);
        if (!vezIA) return;   

        long inicio = System.currentTimeMillis();
        Node raiz = new Node();
        Arvore arvore = new Arvore();
        Tabuleiro clone = tabuleiroLogico.clone();

        arvore.setIA(corIA);
        raiz.setMatrix(clone.getMatriz());
        raiz.setTurn(controle.turnoBranca);
        
        arvore.montarArvoreIA(raiz, clone, profundidade, controle.turnoBranca);
        System.out.println("melhor node é:     " + arvore.getMelhorMinMax());
        System.out.println("Nós gerados: " + arvore.contarNos(raiz) + " em " + (System.currentTimeMillis() - inicio) + "ms. Melhor MinMax: " + arvore.getMelhorMinMax());        

        Node melhorJogada = arvore.getMelhorFilho();
        boolean sucesso;
        if(melhorJogada == null)
            return;

        PosicaoReal origem = controle.decodificarCasa(melhorJogada.getOrigin());
        PosicaoReal fim = controle.decodificarCasa(melhorJogada.getDest());
        sucesso = moverPecaLogica(origem.linha, origem.coluna, fim.linha, fim.coluna);
        System.out.println("A jogada da ia será: [" + origem.linha + ", " + origem.coluna + "] até: [" + fim.linha + ", " + fim.coluna + "]");

        if(sucesso){
            sincronizarInterface();
            
            if(tabuleiroLogico.isLocked())
                fazerJogadaIA();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainInterfaceGrafica::new);
    }

    public void sincronizarInterface() {

    /*
     * Atualiza a interface gráfica com base na matriz lógica do Tabuleiro. Este
     * método será chamado após cada jogada da IA.
     */

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                char peca = tabuleiroLogico.getMatriz()[i][j];
                tabuleiroInterface[i][j].setTipoPeca(peca);
            }
        }
    }

    private class CasaBotao extends JButton {

        private char tipoPeca = '0';

        public void setTipoPeca(char tipo) {
            this.tipoPeca = tipo;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int margem = 10;
            // Brancas
            if (tipoPeca == '1' || tipoPeca == '3') { 
                g2.setColor(Color.WHITE);
                g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
                g2.setColor(Color.BLACK);
                g2.drawOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
            // Pretas
            } else if (tipoPeca == '2' || tipoPeca == '4') { 
                g2.setColor(Color.BLACK);
                g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
            }

            // Representação de Dama (uma borda dourada)
            if (tipoPeca > '2') { 
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(margem + 5, margem + 5, getWidth() - 2 * margem - 10, getHeight() - 2 * margem - 10);
            }
        }
    }
}
