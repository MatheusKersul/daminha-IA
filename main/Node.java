package main;

import java.util.ArrayList;

/**
 *
 * @author Douglas
 */
public class Node {
    
    private char origin;
    private char dest;
    private boolean turn; //TRUE - white; FALSE - black;
    private boolean nextTurn;
    private char[][] matrix;
    private int minMax;
    private ArrayList<Node> children;
    private boolean valido;
    private Node pai;
    
    public Node () {
        this.children = new ArrayList<>();
        this.minMax = Integer.MIN_VALUE;
    }

    public int getMinMax() {
        return minMax;
    }

    public void setPai(Node pai){

        this.pai = pai;
    }

    public Node getPai(){

        return pai;
    }

    public void setMinMax(int minMax) {
        this.minMax = minMax;
    }
    
    public ArrayList<Node> getChild (){
        return this.children;
    }
    
    public void addChild (Node child){
        this.children.add(child);
    }
    
    public char getOrigin() {
        return origin;
    }

    public void setOrigin(char origin) {
        this.origin = origin;
    }

    public char getDest() {
        return dest;
    }

    public void setDest(char dest) {
        this.dest = dest;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;
    }
    
    public void setNextTurn(boolean turn){
        
        this.nextTurn = turn;
    }

    public boolean getNextTurn(){
        return nextTurn;
    }

    public void setValido (boolean valido){
        this.valido = valido;
    }
    
    public boolean getValido(){
        return valido;
    }
    
}