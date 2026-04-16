package main;

import javax.swing.*;
import main.Control.PosicaoReal;
import java.awt.*;
import java.util.ArrayList;
import main.Control.Jogada;

public final class MainInterfaceGrafica extends JFrame {

    private final int TAMANHO = 6;
    private final CasaBotao[][] tabuleiroInterface = new CasaBotao[TAMANHO][TAMANHO];
    Control controle = new Control();
    private final Tabuleiro tabuleiroLogico;
    private char caracterCasa = 'A'; 
    private int linhaOrigem = -1, colOrigem = -1;
    private int profundidade = 10;
    private int empate = 1;
    private boolean poda = false;
    private boolean corIA;
    private int opcaoIA;

    private JLabel labelNos;
    private JLabel labelMinMax;
    private JLabel labelJogada;
    private JSlider sliderDificuldade;
    private JLabel labelDificuldade;

    public MainInterfaceGrafica() {

        String[] opcoes = {"branca", "preta"};
        opcaoIA = JOptionPane.showOptionDialog(null, "A IA será qual peça?", "Escolha da IA", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, null);
       
        tabuleiroLogico = new Tabuleiro(controle);
        setTitle("DISCIPLINA - IA - MINI JOGO DE DAMA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout principal: painel lateral + tabuleiro
        setLayout(new BorderLayout());

        // === PAINEL LATERAL ESQUERDO ===
        JPanel painelLateral = new JPanel();
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setBackground(new Color(30, 30, 30));
        painelLateral.setPreferredSize(new Dimension(180, 500));
        painelLateral.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Título
        JLabel titulo = new JLabel("IA - DAMA");
        titulo.setForeground(new Color(220, 180, 80));
        titulo.setFont(new Font("Monospaced", Font.BOLD, 16));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Separador
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(80, 80, 80));
        sep1.setMaximumSize(new Dimension(160, 2));

        // Dificuldade
        JLabel lblDif = new JLabel("DIFICULDADE");
        lblDif.setForeground(new Color(150, 150, 150));
        lblDif.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblDif.setAlignmentX(Component.LEFT_ALIGNMENT);

        sliderDificuldade = new JSlider(1, 15, profundidade);
        sliderDificuldade.setBackground(new Color(30, 30, 30));
        sliderDificuldade.setForeground(new Color(220, 180, 80));
        sliderDificuldade.setMaximumSize(new Dimension(160, 40));
        sliderDificuldade.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderDificuldade.addChangeListener(e -> {
            if (!sliderDificuldade.getValueIsAdjusting()) {
                profundidade = sliderDificuldade.getValue();
                labelDificuldade.setText("Profundidade: " + profundidade);
            }
        });

        labelDificuldade = new JLabel("Profundidade: " + profundidade);
        labelDificuldade.setForeground(new Color(220, 180, 80));
        labelDificuldade.setFont(new Font("Monospaced", Font.BOLD, 12));
        labelDificuldade.setAlignmentX(Component.LEFT_ALIGNMENT);
        JCheckBox checkPoda = new JCheckBox("Habilitar Poda", poda);
        checkPoda.setBackground(new Color(30, 30, 30));
        checkPoda.setForeground(new Color(220, 180, 80));
        checkPoda.setFont(new Font("Monospaced", Font.PLAIN, 11));
        checkPoda.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkPoda.setFocusPainted(false);
        checkPoda.addActionListener(e -> {
            poda = checkPoda.isSelected();
        });

        // Separador
        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(80, 80, 80));
        sep2.setMaximumSize(new Dimension(160, 2));

        // Stats da IA
        JLabel lblStats = new JLabel("ÚLTIMA JOGADA");
        lblStats.setForeground(new Color(150, 150, 150));
        lblStats.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblStats.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelNos = new JLabel("Nós: -");
        labelNos.setForeground(Color.WHITE);
        labelNos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelNos.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelMinMax = new JLabel("MinMax: -");
        labelMinMax.setForeground(Color.WHITE);
        labelMinMax.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelMinMax.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelJogada = new JLabel("<html>Jogada:<br>-</html>");
        labelJogada.setForeground(new Color(100, 220, 100));
        labelJogada.setFont(new Font("Monospaced", Font.BOLD, 12));
        labelJogada.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Monta o painel
        painelLateral.add(titulo);
        painelLateral.add(Box.createVerticalStrut(10));
        painelLateral.add(sep1);
        painelLateral.add(Box.createVerticalStrut(10));
        painelLateral.add(lblDif);
        painelLateral.add(Box.createVerticalStrut(5));
        painelLateral.add(sliderDificuldade);
        painelLateral.add(labelDificuldade);
        painelLateral.add(Box.createVerticalStrut(8));
        painelLateral.add(checkPoda);  
        painelLateral.add(Box.createVerticalStrut(15));
        painelLateral.add(sep2);
        painelLateral.add(Box.createVerticalStrut(10));
        painelLateral.add(lblStats);
        painelLateral.add(Box.createVerticalStrut(8));
        painelLateral.add(labelNos);
        painelLateral.add(Box.createVerticalStrut(4));
        painelLateral.add(labelMinMax);
        painelLateral.add(Box.createVerticalStrut(4));
        painelLateral.add(labelJogada);

        // Tabuleiro
        JPanel painelTabuleiro = new JPanel(new GridLayout(TAMANHO, TAMANHO));
        painelTabuleiro.setPreferredSize(new Dimension(500, 500));

        inicializarComponentes(painelTabuleiro);

        add(painelLateral, BorderLayout.WEST);
        add(painelTabuleiro, BorderLayout.CENTER);

        pack();
        setVisible(true);
        sincronizarInterface();
    }
    
    private void setCorIA(){

        int aleatorio = (int)(Math.random() * 2);
        
        if(aleatorio == 0)
            corIA = true;
        else
            corIA = false;
    }

    private void inicializarComponentes(JPanel painelTabuleiro) {

    if(opcaoIA == 0)
        corIA = true;
    
    else if(opcaoIA == 1)
        corIA = false;
    
    else
        setCorIA();
    for (int i = 0; i < TAMANHO; i++) {
        for (int j = 0; j < TAMANHO; j++) {
            tabuleiroInterface[i][j] = new CasaBotao();
            if ((i + j) % 2 == 0) {
                tabuleiroInterface[i][j].setBackground(new Color(255, 255, 255));
            } else {
                controle.setarCasas(caracterCasa, i, j);
                controle.setarChaves(i, j, caracterCasa);
                caracterCasa++;
                tabuleiroInterface[i][j].setBackground(new Color(100, 100, 100));
            }
            int linha = i, coluna = j;
            tabuleiroInterface[i][j].addActionListener(e -> tratarClique(linha, coluna));
            painelTabuleiro.add(tabuleiroInterface[i][j]); // adiciona no painel, não no JFrame
        }
    }
    if (corIA) fazerJogadaIA();
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

            if (tabuleiroLogico.verificarFimPartida()){

                sincronizarInterface();
                int brancas = 0;
                for (char[] linha : tabuleiroLogico.getMatriz())
                    for (char c : linha) {
                        if (c == '1' || c == '3') brancas++;
                    }

                String msg = (brancas == 0) ? "Vitória das pretas!" : "Vitória das brancas!";
                JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                return true;
            }
            
            else if(tabuleiroLogico.veririficarAfogamento(tabuleiroLogico, controle.turnoBranca)){

                    if(empate == 0){

                        String msg = (controle.turnoBranca) ? "Vitória das pretas!" : "Vitória das brancas!";
                        JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                        return true;
                    }
                    else
                        empate--;
            }
            else if(tabuleiroLogico.verificarEmpate()){

                if(empate == 0){

                    JOptionPane.showMessageDialog(this, "Empate!! Somente duas damas no tabuleiro", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    return true;
                }
                else
                    empate--;
            }

            return true;
        }
        return false;
    }

    private void fazerJogadaIA(){

        boolean vezIA = (controle.turnoBranca && corIA) || (!controle.turnoBranca && !corIA);
        if (!vezIA) return;   

        Node raiz = new Node();
        Arvore arvore = new Arvore();
        Tabuleiro clone = tabuleiroLogico.clone();

        arvore.setIA(corIA);
        raiz.setMatrix(clone.getMatriz());
        raiz.setTurn(controle.turnoBranca);
        
        if(poda)
            arvore.montarArvoreIA(raiz, clone, profundidade, controle.turnoBranca, Integer.MIN_VALUE, Integer.MAX_VALUE);
        else
            arvore.montarArvoreIA(raiz, clone, profundidade, controle.turnoBranca);

        long nos = arvore.getTotalNos();
        labelNos.setText("Nós: " + nos);
        int melhorminmax = arvore.getMelhorMinMax();

        if (melhorminmax < 0 )
            labelMinMax.setText("MinMax: " + -arvore.getMelhorMinMax());
        else
            labelMinMax.setText("MinMax: " + arvore.getMelhorMinMax());

        Node melhorJogada = arvore.getMelhorFilho();
        boolean sucesso;
        PosicaoReal origem = controle.decodificarCasa(melhorJogada.getOrigin());
        PosicaoReal fim = controle.decodificarCasa(melhorJogada.getDest());


        labelJogada.setText("<html>Jogada:<br>[" + origem.linha + ", " + origem.coluna + "]" +
                    "<br>para<br>[" + fim.linha + ", " + fim.coluna + "]</html>");


        sucesso = moverPecaLogica(origem.linha, origem.coluna, fim.linha, fim.coluna);

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
