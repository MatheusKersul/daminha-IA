package main;

import java.util.ArrayList;

import main.Control.Jogada;
public class Tabuleiro implements Cloneable {

    private char[][] matriz;
    private final int TAMANHO = 6;
    Control controle;
    char caracterCasa = 'a';
    int linhaTravada;
    int colunaTravada;
    boolean trava;
    ArrayList<Jogada> todasJogadas;

    public Tabuleiro(Control controle) {
        this.matriz = new char[TAMANHO][TAMANHO];
        this.controle = controle;
        inicializar();
    }

    private void travaPeca(int linha, int coluna){

        this.linhaTravada = linha;
        this.colunaTravada = coluna;
        this.trava = true;
    }

    public boolean isLocked(){

        return this.trava;
    }

    private void inicializar() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) {

                    matriz[i][j] = '0';
                    if (i < 2) {
                        matriz[i][j] = '2'; // Pretas

                    } else if (i > 3) {
                        matriz[i][j] = '1'; // Brancas

                    }
                }
            }
        }
    }  
    
    @Override
    public Tabuleiro clone(){
        try {
            Tabuleiro clone = (Tabuleiro) super.clone();
            clone.matriz = new char[TAMANHO][];
            for (int i = 0; i < TAMANHO; i++) {
                clone.matriz[i] = this.matriz[i].clone();
            }
            
        clone.controle      = this.controle; 
        clone.linhaTravada  = this.linhaTravada;
        clone.colunaTravada = this.colunaTravada;
        clone.trava         = this.trava;


            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public void setJogadas(ArrayList<Jogada> jogadas){

        this.todasJogadas = jogadas;
    }

    public ArrayList<Jogada> getJogadas(){

        return todasJogadas;
    }

    public boolean verificarFimPartida(){

        int brancas = 0, pretas = 0;
        int dBranca = 0, dPreta = 0;
        for (char[] linha : matriz){
            for (char casa : linha) {
                if (casa == '1') brancas++;
                if (casa == '3') dBranca++;
                if (casa == '2') pretas++;
                if (casa == '4') dPreta++;
            }
        }
        
        // VERIFICAÇÃO PARA O JOGO SER INTERROMPIDO POR CAUSA DAS DAMAS  || (dBranca == 1 && dPreta == 1 && (brancas == 0 && pretas == 0)
        return ((brancas == 0 && dBranca == 0) || (pretas == 0 && dPreta ==0));
    }

    public boolean veririficarAfogamento(Tabuleiro tabuleiro, boolean turno){

        ArrayList<Jogada> jogadas =  getMovimentosPossiveis(tabuleiro, turno);

        if(jogadas.isEmpty()){
            return true; //sem jogadas disponivieis para aquele turno
        }
        else{
            return false;
        }
    }

    public ArrayList<Jogada> getMovimentosPossiveis(Tabuleiro clone, boolean turno) {
   
        ArrayList<Jogada> movimentos = new ArrayList<>();
        for (int i = 0; i < 6; i++){

            int j = (i % 2 == 0) ? 1 : 0;
            for (; j < 6; j += 2){

                char casa = clone.matriz[i][j];
                if(turno && casa == '1')
                    movimentos.addAll(getMovimentoPecas(clone, i, j, clone.isLocked()));
                
                else if(!turno && casa == '2')
                    movimentos.addAll(getMovimentoPecas(clone, i, j, clone.isLocked()));
                
                else if((turno && casa == '3') || (!turno && casa == '4'))
                    movimentos.addAll(getMovimentoDamas(clone, i, j));
            }
        }

    return movimentos;
}

    public ArrayList<Control.Jogada> getMovimentoPecas(Tabuleiro clone, int linha, int coluna, boolean locked){

        
            ArrayList<Control.Jogada> movimentos = new ArrayList<>();
            char peca = clone.matriz[linha][coluna];
            int[] dLin;
            int[] dCol;

            if(peca == '1'){

                if (!clone.isLocked()){

                    dLin = new int[]{-1, -1, -2, -2};
                    dCol = new int[]{-1, 1, -2, 2};
                }
                else{

                    dLin = new int[]{-1, -1, -2, -2, 1, 1, 2, 2};
                    dCol = new int[]{-1, 1, -2, 2, -1, 1, -2, 2};
                }
                for (int diagonal = 0; diagonal < dLin.length; diagonal++){

                    int posLin = linha + dLin[diagonal];
                    int posCol = coluna + dCol[diagonal];

                    if (posCol >= 0 && posCol <= 5 && posLin >= 0 && posLin <= 5){

                        Tabuleiro teste = clone.clone();
                        if(teste.verificaMovimento(linha, coluna, posLin, posCol)){
                            
                            // 1. Efetiva o movimento no tabuleiro clonado da IA
                            teste.getMatriz()[posLin][posCol] = teste.getMatriz()[linha][coluna];
                            teste.getMatriz()[linha][coluna] = '0';

                            // 2. Faz a promoção para dama no tabuleiro clonado
                            if (teste.getMatriz()[posLin][posCol] == '2' && posLin == 5){
                                teste.getMatriz()[posLin][posCol] = '4';
                            }
                            if (teste.getMatriz()[posLin][posCol] == '1' && posLin == 0){
                                teste.getMatriz()[posLin][posCol] = '3';
                            }
                            
                            //salva a jogada
                            movimentos.add(controle.new Jogada(
                            controle.new PosicaoReal(linha, coluna),
                            controle.new PosicaoReal(posLin, posCol), teste
                            )); 
                    }
                    }
                }
            }

            else{

                if(!clone.isLocked()){

                    //fazer isso
                    dLin = new int[]{1, 1, 2, 2};
                    dCol = new int[]{-1, 1, -2, 2};

                }
                else{

                    dLin = new int[]{1, 1, 2, 2, -1, -1, -2, -2};
                    dCol = new int[]{-1, 1, -2, 2, -1, 1, -2, 2};
                }
                for (int diagonal = 0; diagonal < dLin.length; diagonal++){

                    int posLin = linha + dLin[diagonal];
                    int posCol = coluna + dCol[diagonal];

                    if (posCol >= 0 && posCol <= 5 && posLin >= 0 && posLin <= 5){

                        Tabuleiro teste = clone.clone();
                        if(teste.verificaMovimento(linha, coluna, posLin, posCol)) {
                        
                        // 1. Efetiva o movimento no tabuleiro clonado da IA
                        teste.getMatriz()[posLin][posCol] = teste.getMatriz()[linha][coluna];
                        teste.getMatriz()[linha][coluna] = '0';

                        // 2. Faz a promoção para dama no tabuleiro clonado (se aplicável)
                        if (teste.getMatriz()[posLin][posCol] == '2' && posLin == 5) {
                            teste.getMatriz()[posLin][posCol] = '4';
                        }
                        if (teste.getMatriz()[posLin][posCol] == '1' && posLin == 0) {
                            teste.getMatriz()[posLin][posCol] = '3';
                        }

                        // 3. Salva a jogada com o tabuleiro no estado futuro real
                        movimentos.add(controle.new Jogada(
                            controle.new PosicaoReal(linha, coluna),
                            controle.new PosicaoReal(posLin, posCol), teste
                        )); 
                    }
                    }
                }
            }

        return movimentos;
            
    }

    public ArrayList<Control.Jogada> getMovimentoDamas(Tabuleiro clone, int linha, int coluna){

        ArrayList<Jogada> movimentos = new ArrayList<>();
        
        int[][] diagonais = {{-1,-1}, {-1,1}, {1,-1}, {1,1}};
        
        for (int[] diag : diagonais) {
            // Anda até bater na borda ou em alguma peça
            int posLin = linha + diag[0];
            int posCol = coluna + diag[1];
            
            while (posLin >= 0 && posLin < 6 && posCol >= 0 && posCol < 6){

                Tabuleiro teste = clone.clone();
                if (teste.verificaMovimento(linha, coluna, posLin, posCol))
                    movimentos.add(controle.new Jogada(
                    controle.new PosicaoReal(linha, coluna),
                    controle.new PosicaoReal (posLin, posCol), teste
                    ));
                
                posLin += diag[0];
                posCol += diag[1];
            }
        }
            return movimentos;

    }

    public void deletarPeca(int linha, int coluna, char pecaJogada, int poslin, int poscol){

        
        matriz[linha][coluna] = '0';
        if (verificacaoJogarNovamente(poslin, poscol, pecaJogada, true)){

            travaPeca(poslin, poscol);
        }
        else{
            this.trava = false;
        }
    }

    public boolean verificacaoSimplesBrancas(int linAtual, int colAtual, int posLin, int posCol){

        return posLin == linAtual - 1 && (posCol == colAtual - 1 || posCol == colAtual + 1) && matriz[posLin][posCol] == '0';
    }

    public boolean verificacaoSimplesPretas(int linAtual, int colAtual, int posLin, int posCol){

        return posLin == linAtual + 1 && (posCol == colAtual - 1 || posCol == colAtual + 1) && matriz[posLin][posCol] == '0';
    }

    public boolean verificacaoJogarNovamente(int linha, int coluna, char peca, boolean combo) {

        if (peca == '1' || peca == '2') {

            int[] linhas = {-1, -1, 1, 1};
            int[] colunas = {-1, 1, -1, 1};

            for (int i = 0; i < 4; i++) {

                if (!combo) {
                    if (peca == '1' && linhas[i] > 0) continue; // Branca não olha para baixo
                    if (peca == '2' && linhas[i] < 0) continue; // Preta não olha para cima
                }
                int linInimiga = linha + linhas[i];
                int colInimiga = coluna + colunas[i];
                int linDestino = linha + (linhas[i] * 2);
                int colDestino = coluna + (colunas[i] * 2);

                if (linDestino >= 0 && linDestino < 6 && colDestino >= 0 && colDestino < 6) {
                    
                    char casaInimiga = matriz[linInimiga][colInimiga];
                    char casaDestino = matriz[linDestino][colDestino];

                    boolean ehInimigo = false;
                    if (peca == '1' && (casaInimiga == '2' || casaInimiga == '4')) ehInimigo = true;
                    if (peca == '2' && (casaInimiga == '1' || casaInimiga == '3')) ehInimigo = true;


                    if (ehInimigo && casaDestino == '0') {
                        return true; 
                    }
                }
            }
            return false; 
        }

            if (peca == '3' || peca == '4'){

            int[] linhas = {-1, -1,  1,  1};
            int[] colunas = {-1,  1, -1,  1};

            for (int d = 0; d < 4; d++){
                int linhaAtual = linha + linhas[d];
                int colunaAtual = coluna + colunas[d];

                while (linhaAtual >= 0 && linhaAtual < 6 && colunaAtual >= 0 && colunaAtual < 6){
                    char casa = matriz[linhaAtual][colunaAtual];
                    if (casa != '0'){
                        boolean ehInimigo = (peca == '3') ? (casa == '2' || casa == '4')
                                                        : (casa == '1' || casa == '3');
                        if (ehInimigo){
                            int lApos = linhaAtual + linhas[d];
                            int cApos = colunaAtual + colunas[d];
                            if (lApos >= 0 && lApos < 6 && cApos >= 0 && cApos < 6
                                    && matriz[lApos][cApos] == '0') {
                                return true;
                            }
                        }
                        break;
                    }
                    linhaAtual += linhas[d];
                    colunaAtual += colunas[d];
                }
            }
            return false;
        }
        return false;
    }

    public boolean verificarObrigatoriedade(boolean turnoBranca){

        for(int i = 0; i < 6; i ++){
            
            int j = (i % 2 == 0) ? 1 : 0;
            for(; j < 6; j += 2){

                char peca = matriz[i][j];

                if(turnoBranca && (peca == '1' || peca == '3')){

                    if(verificacaoJogarNovamente(i, j, peca, false))
                        return true;
                }
                else if(!turnoBranca && (peca =='2' || peca =='4')){

                    if(verificacaoJogarNovamente(i, j, peca, false))
                        return true;

                }
            }
        }
        
        return false;
    }

    public boolean verificaMovimento (int linAtual, int colAtual, int posLin, int posCol) {
        
        char peca = matriz[linAtual][colAtual];
        boolean obrigatoriedade = verificarObrigatoriedade(peca == '1' || peca == '3');

        if (isLocked()){        //verifica se alguma peça é obrigada a comer

            if(linhaTravada != linAtual ||  colunaTravada != colAtual){
            
                return false;
            }
        }

        if (peca == '1'){           //controle das brancas
            
            if (!isLocked() && !obrigatoriedade && verificacaoSimplesBrancas(linAtual, colAtual, posLin, posCol))
                return true;       
            
            else if ((posLin == linAtual - 2 || (isLocked() && posLin == linAtual + 2)) && Math.abs(colAtual - posCol) == 2 && matriz[posLin][posCol] == '0'){
                
                // Encontra a linha e a coluna exata da peça que está sendo pulada
                int linInimiga = (linAtual + posLin) / 2;
                int colInimiga = (colAtual + posCol) / 2;
                char valorCasa = matriz[linInimiga][colInimiga];
                   
                if (valorCasa == '2' || valorCasa == '4'){
                    deletarPeca(linInimiga, colInimiga, peca, posLin, posCol);
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        
        if(peca == '2'){            //controle das pretas
            
            if (!isLocked() && !obrigatoriedade && verificacaoSimplesPretas(linAtual, colAtual, posLin, posCol))         
                return true;       

            else if ( (posLin == linAtual + 2 || (isLocked() && posLin == linAtual - 2)) && Math.abs(colAtual - posCol) == 2 && matriz[posLin][posCol] == '0'){
                
                // Encontra a linha e a coluna exata da peça que está sendo pulada
                int linInimiga = (linAtual + posLin) / 2;
                int colInimiga = (colAtual + posCol) / 2;
                char valorCasa = matriz[linInimiga][colInimiga];

                if (valorCasa == '1' || valorCasa == '3'){
                    deletarPeca(linInimiga, colInimiga, peca, posLin, posCol);
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        
        if (peca == '3' || peca == '4'){        //controle das damas
            int distLin = Math.abs(posLin - linAtual);
            int distCol = Math.abs(posCol - colAtual);

            // 1. Valida se é diagonal
            if (distLin != distCol) return false;

            int stepLin = (posLin > linAtual) ? 1 : -1;
            int stepCol = (posCol > colAtual) ? 1 : -1;

            // 2. Movimento Simples (sem captura)
            int pecasNoCaminho = 0;
            int linInimiga = -1, colInimiga = -1;

            for (int i = 1; i < distLin; i++){
                int l = linAtual + (i * stepLin);
                int c = colAtual + (i * stepCol);
                if (matriz[l][c] != '0') {
                    pecasNoCaminho++;
                    linInimiga = l;
                    colInimiga = c;
                }
            }

            if (!isLocked() && !obrigatoriedade && pecasNoCaminho == 0 && matriz[posLin][posCol] == '0') {
                return true; 
            }

            if (pecasNoCaminho == 1){
                // Verifica se a peça no caminho é inimiga
                char alvo = matriz[linInimiga][colInimiga];
                boolean ehInimigo = (peca == '3') ? (alvo == '2' || alvo == '4') : (alvo == '1' || alvo == '3');

                if (ehInimigo){
                    if (posLin == linInimiga + stepLin && posCol == colInimiga + stepCol) {
                        if (matriz[posLin][posCol] == '0') {

                            if(isLocked() && (linhaTravada != linAtual || colunaTravada != colAtual))
                                return false;

                            deletarPeca(linInimiga, colInimiga, peca, posLin, posCol);
                            return true;
                        }
                    }
                }
            }

            return false; 
        }
        
        return false;
    }

    public char[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(char[][] matriz) {
        this.matriz = matriz;
    }
}
