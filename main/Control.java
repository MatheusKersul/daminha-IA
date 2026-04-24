package main;

import java.util.HashMap;
import java.util.Objects;

public class Control {
    
    public boolean turnoBranca = true;

    public int pecasBrancas = 6;
    public int pecasPretas = 6;
    public int damasBrancas = 0;
    public int damasPretas = 0;

    HashMap<Character, PosicaoReal> decodificador = new HashMap<>();
    HashMap<PosicaoReal, Character> codificador = new HashMap<>();
    
    public void setarCasas(char c, int linha, int coluna){

        decodificador.put(c, new PosicaoReal(linha, coluna));

    }

    public void setarChaves(int linha, int coluna, char c){

        codificador.put(new PosicaoReal(linha, coluna), c);
    }
    /*
    *   recebe como parametro a localização dentro da matriz
    *   devolve o char que indica a casa do tabuleiro  
    */
    public char codificarCasa(int linha, int coluna){

        PosicaoReal posicao = new PosicaoReal(linha, coluna);
        
        return codificador.get(posicao);

    }

    public PosicaoReal decodificarCasa(char casa){

        return decodificador.get(casa);
    }
    /*        recebe o char que representa a casa
        devolve a posição dentro da matriz
    */
    public PosicaoReal getEnderecoCasa(char key){

        PosicaoReal endereco = decodificador.get(key);
        return endereco;
    }

    class Jogada{


        private char casaInicio;
        private char casaFim;
        private Tabuleiro clone;

        public Jogada(PosicaoReal inicio, PosicaoReal fim, Tabuleiro tabuleiro){

            this.casaInicio = codificarCasa(inicio.linha, inicio.coluna);
            this.casaFim = codificarCasa(fim.linha, fim.coluna);
            this.clone = tabuleiro;
        }

        public char getOrigin(){

            return casaInicio;
        }

        public char getDest(){

            return casaFim;
        }

        public Tabuleiro getTabuleiro(){

            return clone;
        }



    }

    public int possiveisMovimentos(int linAtual, int colAtual){


        return 0;
    }

    public boolean getTurno(char peca){


        if((peca == '1' || peca == '3') && turnoBranca)
            
            return true;

        if((peca == '2' || peca == '4') && !turnoBranca)

            return true;
        
        return false;
    }

    public void mudarJogada(){

        if (turnoBranca)
            this.turnoBranca = false;
        else
            this.turnoBranca = true;
    }

    class PosicaoReal{

        int linha;
        int coluna;

        public PosicaoReal(int linha, int coluna){

            this.linha = linha;
            this.coluna = coluna;
        }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PosicaoReal)) return false;
        PosicaoReal outro = (PosicaoReal) obj;
        return this.linha == outro.linha && this.coluna == outro.coluna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(linha, coluna);
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
    }
}
