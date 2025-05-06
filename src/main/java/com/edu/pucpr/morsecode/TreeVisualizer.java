package com.edu.pucpr.morsecode;

import com.sun.tools.jconsole.JConsoleContext;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;

public class TreeVisualizer extends Application {

    // Classe Node para a árvore binária de busca
    static class Node {

        public Node (char letter) {
            this.letter = letter;
        }

        char letter;
        Node left;
        Node right;
    }

    // Classe da árvore binária de busca
    static class MorseBST {

        private Node root;

        public MorseBST() {
            this.root = new Node(' ');

            insert('A', ".-", this.root);
            insert('B', "-...", this.root);
            insert('C', "-.-.", this.root);
            insert('D', "-..", this.root);
            insert('E', ".", this.root);
            insert('F', "..-.", this.root);
            insert('G', "--.", this.root);
            insert('H', "....", this.root);
            insert('I', "..", this.root);
            insert('J', ".---", this.root);
            insert('K', "-.-", this.root);
            insert('L', ".-..", this.root);
            insert('M', "--", this.root);
            insert('N', "-.", this.root);
            insert('O', "---", this.root);
            insert('P', ".--.", this.root);
            insert('Q', "--.-", this.root);
            insert('R', ".-.", this.root);
            insert('S', "...", this.root);
            insert('T', "-", this.root);
            insert('U', "..-", this.root);
            insert('V', "...-", this.root);
            insert('W', ".--", this.root);
            insert('X', "-..-", this.root);
            insert('Y', "-.--", this.root);
            insert('Z', "--..", this.root);


            decode_morse(this.root, "--- .-.. .- / . ..- / ... --- ..- / --- / -- .. --. ..- . .-..");
            encode_morse(this.root, "OLA EU SOU O MIGUEL");
        }

        public String encode_morse(Node root, String texto) {
            StringBuilder encoded = new StringBuilder();
            String[] words = texto.toUpperCase().split(" ");

            for (int i = 0; i < words.length; i++) {
                String word = words[i];

                for (int j = 0; j < word.length(); j++) {
                    char wordChar = word.charAt(j);
                    StringBuilder morseCode = new StringBuilder();

                    if (char_to_morse(root, wordChar, morseCode)) {
                        encoded.append(morseCode);
                        if (j < word.length() - 1) {
                            encoded.append(' ');
                        }
                    }
                }

                if (w < words.length - 1) {
                    encoded.append(" / ");
                }
            }

            System.out.println(encoded.toString());
            return encoded.toString();
        }
        private boolean char_to_morse(Node node, char target, StringBuilder morseCode) {
            if (node == null)
                return false;

            if (node.letter == target) {
                return true;
            }

            morseCode.append('.');

            if (char_to_morse(node.left, target, morseCode)) {
                return true;
            }

            morseCode.setLength(morseCode.length() - 1);

            morseCode.append('-');

            if (char_to_morse(node.right, target, morseCode)) {
                return true;
            }
            morseCode.setLength(morseCode.length() - 1);

            return false;
        }


        public char morse_to_char(Node root, char[] sequence, int i) {
            if (i == sequence.length) {
                return root.letter;
            } else if (String.valueOf(sequence[i]).equals(".")) {
                return morse_to_char(root.left, sequence, i+1);
            } else {
                return morse_to_char(root.right, sequence, i+1);
            }
        }

        public String decode_morse (Node root, String str) {
            String decoded = "";
            String[] sequences = str.split(" ");
            for (String sequence : sequences) {
                if (sequence.equals("/")) {
                    decoded += " ";
                } else {
                    char[] sequenceChar = sequence.toCharArray();
                    decoded += morse_to_char(root, sequenceChar, 0);
                }
            }

            System.out.println(decoded);
            return decoded;
        }


        public void insert(char letter, String morseCode, Node node) {
            // Inserir lógica de inserção: ponto (.) para a esquerda e traço (-) para a direita

            if (morseCode.isEmpty()) {
                node.letter = letter;
                return;
            }

            String[] morseCodes = morseCode.split("");

            String element = morseCodes[0];

            if (element.equals(".")) {
                if (node.left == null) {
                    char letterRoot = ' ';
                    node.left = new Node(letterRoot);
                }

                String[] newMorseCodes = Arrays.copyOfRange(morseCodes, 1, morseCode.length());
                StringBuilder morseCodeNew = new StringBuilder();

                for (String morseCodeChar : newMorseCodes) {
                    morseCodeNew.append(morseCodeChar);
                }

                insert(letter, morseCodeNew.toString(), node.left);

            } else {
                if (node.right == null) {
                    char letterRoot = ' ';
                    node.right = new Node(letterRoot);
                }

                String[] newMorseCodes = Arrays.copyOfRange(morseCodes, 1, morseCode.length());
                StringBuilder morseCodeNew = new StringBuilder();

                for (String morseCodeChar : newMorseCodes) {
                    morseCodeNew.append(morseCodeChar);
                }

                insert(letter, morseCodeNew.toString(), node.right);
            }
        }

        // Calcula a altura da árvore
        public int getHeight() {
            return getHeight(root);
        }

        private int getHeight(Node node) {
            if (node == null) {
                return 0;
            }
            return 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }

        public void drawTree(Canvas canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);

            // Começa o desenho da árvore na raiz
            drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4, 1);
        }

        private void drawNode(GraphicsContext gc, Node node, double x, double y, double xOffset, int level) {
            if (node == null) {
                return;
            }

            // Desenha um círculo ao redor do nó
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 15, y - 15, 30, 30); // Desenha o círculo com raio 15

            // Desenha a letra dentro do círculo
            gc.strokeText(String.valueOf(node.letter == ' ' ? ' ' : node.letter), x - 5, y + 5);

            // Desenho das linhas para os nós filhos
            if (node.left != null) {
                double newX = x - xOffset;
                double newY = y + 120; // Aumentei o espaçamento vertical
                gc.strokeLine(x, y + 15, newX, newY - 15); // Linha entre o nó atual e o filho à esquerda
                drawNode(gc, node.left, newX, newY, xOffset / 2, level + 1);
            }

            if (node.right != null) {
                double newX = x + xOffset;
                double newY = y + 120; // Aumentei o espaçamento vertical
                gc.strokeLine(x, y + 15, newX, newY - 15); // Linha entre o nó atual e o filho à direita
                drawNode(gc, node.right, newX, newY, xOffset / 2, level + 1);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Visualizador de Árvore Binária");

        MorseBST bst = new MorseBST();

        // Inicialização de Janela
        int height = bst.getHeight();
        int canvasHeight = 100 + height * 100;
        int canvasWidth = (int) Math.pow(2, height) * 40;

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        bst.drawTree(canvas);

        Group root = new Group();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, canvasWidth, canvasHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
