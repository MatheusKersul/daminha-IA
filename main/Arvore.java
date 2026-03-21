package main;

import main.Control.Jogada;
import java.util.ArrayList;

public class Arvore{
    
    boolean IA;
    int melhorMinMax;
    Node melhorFilho;

    public void setIA(boolean IA){

        this.IA = IA;
    }

    public void setMelhorMinMax(int novo, Node melhorFilho){

        if (melhorMinMax == 0 || novo > melhorMinMax){
            this.melhorMinMax = novo;
            setMelhorFilho(melhorFilho);
        }
    }

    public int getMelhorMinMax(){
        return melhorMinMax;
    }

    public boolean getIA(){
        return IA;
    }

    public int montarArvoreIA(Node no, Tabuleiro tabuleiro, int profundidade, boolean turno){

        if (profundidade <= 0){

            int valor = estadoTabuleiro(no.getMatrix(), IA);
            no.setMinMax(valor);
            return valor;
        }

            
        ArrayList<Jogada> jogadasPossiveis = tabuleiro.getMovimentosPossiveis(tabuleiro, turno);
        
        if (jogadasPossiveis.isEmpty()){
            int valor = estadoTabuleiro(no.getMatrix(), IA);
            no.setMinMax(valor);
            return valor;
        }

        boolean maximizando = (turno == IA);

        //se for o turno da ia, ela tenta maximizar a jogada, senão ela tenta minimizar.
        //qualquer valor de inicio será melhor que o minvalue, então ele servirá como comparação
        int melhorValor = maximizando ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Node melhorFilhoLocal = null;

        for (Jogada jogada : jogadasPossiveis){

            Node filho = new Node();
            filho.setPai(no);
            filho.setOrigin(jogada.getOrigin());
            filho.setDest(jogada.getDest());
            filho.setMatrix(jogada.getTabuleiro().getMatriz());
            filho.setTurn(turno);

            boolean proximoTurno = jogada.getTabuleiro().isLocked() ? turno : !turno;
            filho.setNextTurn(proximoTurno);
            no.addChild(filho);

            int valorFilho =  montarArvoreIA(filho, jogada.getTabuleiro(), profundidade - 1, proximoTurno);
            if(maximizando){

                if(valorFilho > melhorValor){   //que começa como minimo se estiver maximizando

                    melhorValor = valorFilho;
                    melhorFilhoLocal = filho;
                }
            }
            else{

                if(valorFilho < melhorValor){

                    melhorValor = valorFilho;
                    melhorFilhoLocal = filho;
                }
            }
        }
        no.setMinMax(melhorValor);
        
        if (no.getPai() == null && melhorFilhoLocal != null){
            this.melhorFilho = melhorFilhoLocal;
            this.melhorMinMax = melhorValor;
        }

        return melhorValor;
           
    }

    private void setMelhorFilho(Node filho){

        this.melhorFilho = filho;
    }

    public Node getMelhorFilho(){
        return melhorFilho;
    }
    
    private int estadoTabuleiro(char[][] matriz, boolean IA){

        int brancas = 0, pretas = 0, damasBrancas = 0, damasPretas = 0;
        int posicaoBrancas = 0, posicaoPretas = 0;
        
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 6; j++){
                char casa = matriz[i][j];
                if (casa == '1'){ 
                    brancas++;
                    posicaoBrancas += 5 - i;
                }
                if (casa == '3'){
                    brancas++;
                    damasBrancas++;
                }
                if (casa == '2'){ 
                    pretas++;
                    posicaoPretas += 5 + i;
                }
                if (casa == '4'){
                    pretas++;
                    damasPretas++;
                }    
            }
        }

        int pecasIA, pecasJogador, damasIA, damasJogador, posIA, posJogador;
        if(IA){
            pecasIA = brancas;
            damasIA = damasBrancas;
            posIA = posicaoBrancas;

            pecasJogador = pretas;
            damasJogador = damasPretas;
            posJogador = posicaoPretas;
        }
        else{
            pecasIA = pretas;
            damasIA = damasPretas;
            posIA = posicaoPretas;

            pecasJogador = brancas;
            damasJogador = damasBrancas;
            posJogador = posicaoBrancas;
        }

        int estado = (pecasIA - pecasJogador) * 100;
        estado += (damasIA - damasJogador) * 200;
        estado += (posIA - posJogador) * 3; 

        return estado;
    }

    public int contarNos(Node no) {
        int total = 1;
        for (Node filho : no.getChild()) {
            total += contarNos(filho);
        }
        return total;
    }
}
