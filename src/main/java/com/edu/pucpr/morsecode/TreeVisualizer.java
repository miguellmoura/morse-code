package com.edu.pucpr.morsecode;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Scanner;

public class TreeVisualizer extends Application {

    // Classe Node para a árvore binária de busca
    static class Node {
        public Node(char letter) {
            this.letter = letter;
        }
        char letter;
        Node left;
        Node right;
    }

    // Classe da árvore binária de busca para Morse
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
        }

        public void insert(char letter, String morseCode, Node node) {
            if (morseCode.isEmpty()) {
                node.letter = letter;
                return;
            }
            char symbol = morseCode.charAt(0);
            String rest  = morseCode.substring(1);
            if (symbol == '.') {
                if (node.left == null) node.left = new Node(' ');
                insert(letter, rest, node.left);
            } else {
                if (node.right == null) node.right = new Node(' ');
                insert(letter, rest, node.right);
            }
        }

        public String decode_morse(Node root, String str) {
            StringBuilder decoded = new StringBuilder();
            String[] sequences = str.trim().split(" ");
            for (String seq : sequences) {
                if (seq.equals("/")) {
                    decoded.append(' ');
                } else {
                    decoded.append(morse_to_char(root, seq.toCharArray(), 0));
                }
            }
            return decoded.toString();
        }

        private char morse_to_char(Node node, char[] seq, int i) {
            if (i == seq.length) {
                return node.letter;
            }
            if (seq[i] == '.') {
                return morse_to_char(node.left, seq, i + 1);
            } else {
                return morse_to_char(node.right, seq, i + 1);
            }
        }

        public String encode_morse(Node root, String texto) {
            StringBuilder encoded = new StringBuilder();
            String[] words = texto.toUpperCase().trim().split(" ");
            for (int w = 0; w < words.length; w++) {
                String word = words[w];
                for (int i = 0; i < word.length(); i++) {
                    char c = word.charAt(i);
                    StringBuilder path = new StringBuilder();
                    if (char_to_morse(root, c, path)) {
                        encoded.append(path);
                        if (i < word.length() - 1) {
                            encoded.append(' ');
                        }
                    }
                }
                if (w < words.length - 1) {
                    encoded.append(" / ");
                }
            }
            return encoded.toString();
        }

        private boolean char_to_morse(Node node, char target, StringBuilder morseCode) {
            if (node == null) return false;
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

        public int getHeight() {
            return getHeight(root);
        }
        private int getHeight(Node node) {
            if (node == null) return 0;
            return 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }
        public void drawTree(Canvas canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4);
        }
        private void drawNode(GraphicsContext gc, Node node, double x, double y, double offset) {
            if (node == null) return;
            gc.strokeOval(x - 15, y - 15, 30, 30);
            gc.strokeText(String.valueOf(node.letter), x - 5, y + 5);
            if (node.left != null) {
                double nx = x - offset, ny = y + 120;
                gc.strokeLine(x, y + 15, nx, ny - 15);
                drawNode(gc, node.left, nx, ny, offset / 2);
            }
            if (node.right != null) {
                double nx = x + offset, ny = y + 120;
                gc.strokeLine(x, y + 15, nx, ny - 15);
                drawNode(gc, node.right, nx, ny, offset / 2);
            }
        }
    }

    private static String userInput;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite uma frase para CODIFICAR em Morse: ");
        userInput = scanner.nextLine();
        System.out.print("Digite uma sequência Morse para DECODIFICAR: ");
        String morseIn = scanner.nextLine();
        scanner.close();

        MorseInputs.textToEncode = userInput;
        MorseInputs.morseToDecode = morseIn;

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MorseBST bst = new MorseBST();

        System.out.println("Texto original: " + MorseInputs.textToEncode);
        System.out.println("Codificado : " + bst.encode_morse(bst.root, MorseInputs.textToEncode));
        System.out.println("Morse input: " + MorseInputs.morseToDecode);
        System.out.println("Decodificado: " + bst.decode_morse(bst.root, MorseInputs.morseToDecode));

        primaryStage.setTitle("Visualizador de Árvore Binária em Morse");
        int height = bst.getHeight();
        int canvasHeight = 100 + height * 100;
        int canvasWidth  = (int) Math.pow(2, height) * 40;
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);

        bst.drawTree(canvas);

        Group root = new Group(canvas);
        Scene scene = new Scene(root, canvasWidth, canvasHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static class MorseInputs {
        static String textToEncode;
        static String morseToDecode;
    }
}
