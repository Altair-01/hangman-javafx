package com.example.pendufx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloController {
    @FXML
    private Label debut;
    @FXML
    private TextField mot;
    @FXML
    private TextField score;
    @FXML
    private TextField name;

    @FXML
    private GridPane gridPane;

    @FXML
    private Canvas canvas;
    // Définir une variable booléenne pour savoir si le mot a été deviné ou non
    final AtomicBoolean[] wordIsGuessed = {new AtomicBoolean(false)};
    //declaration val
    final AtomicInteger val = new AtomicInteger();
    //declaration de erreur
    final AtomicInteger erreur = new AtomicInteger();
    //Chronomètre
    private int timeElapsed = 0;
    private String previousName = "";
    private static ArrayList<String> motsPredefinis = new ArrayList<>(Arrays.asList("MAISON", "ANANAS", "PARIS", "AVION", "VACANCES", "CHAT", "VITAMINE", "MONTAGNE", "SOURIS", "SAXOPHONE"));
    private static Random rand = new Random();

    public String choisirMotPredefini() {
        int index = rand.nextInt(motsPredefinis.size());
        return motsPredefinis.get(index);
    }

    String motPredefini = "";


    @FXML
    private void onHelloButtonClick(ActionEvent event) throws Exception {
        // Crée une nouvelle boîte de dialogue pour la saisie de texte
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nom d'utilisateur");
        alert.setHeaderText(null);
        alert.setContentText("Entrez votre nom :");

        // Crée un champ de texte dans la boîte de dialogue
        TextField textField = new TextField();
        textField.setPromptText("Nom");

        // Ajoute le champ de texte à une grille
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nom :"), 0, 0);
        grid.add(textField, 1, 0);

        alert.getDialogPane().setContent(grid);

        // Attends que l'utilisateur clique sur le bouton OK ou Annuler
        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Annuler");
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        alert.showAndWait().ifPresent(result -> {
            if (result == buttonTypeOK) {
                String username = textField.getText();
                System.out.println("Nom d'utilisateur : " + username);
                name.setText(username);
                // Continuer avec le programme en utilisant le nom d'utilisateur entré
            } else {
                // L'utilisateur a cliqué sur Annuler, arrêter le programme ou faire autre chose
            }
        });
       reinitialize(previousName);
    }

    // Cette méthode permet de réinitialiser l'état du jeu.
// Elle prend en argument le nom précédent du joueur (ou null s'il n'y en a pas).
    public void reinitialize(String previousName) {

        // On efface le canvas où est dessiné le pendu
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // On remet à zéro le nombre d'erreurs
        erreur.set(0);

        // Si le nom du joueur a changé, on réinitialise le score et la valeur du mot actuel
        if (!name.getText().equals(previousName)) {
            score.setText("0");
            val.set(0);
            this.previousName = name.getText();
        }

        // On réinitialise le mot à deviner et les champs de saisie du joueur
        initialize();
    }
    public void initialize() {
        motPredefini=choisirMotPredefini();
mot.setText(motPredefini);
System.out.println(motPredefini);

        // Obtient le contexte de dessin du Canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int firstX = 30;
        int secondX = -10;
        int thirdX = 90;

        int firstY =280;
        int secondY =310;
        int thirdY =310;

        gc.setFill(Color.MAROON);
        gc.setStroke(Color.BLACK);
        gc.fillPolygon(new double[]{firstX, secondX,thirdX},
                new double[]{firstY, secondY, thirdY}, 3);

        gc.strokePolygon(new double[]{firstX, secondX,thirdX},
                new double[]{firstY, secondY, thirdY}, 3);

        // Dessine une ligne horizontale pour le sommet de la potence
        gc.fillRect(20, 20, 180, 10);

        // Dessine une ligne verticale pour le poteau
        gc.fillRect(20, 20, 20, 280);
        gc.save();
        // Dessine une ligne diagonale pour la barre transversale
        // Rotation de 45 degrés du contexte graphique autour du point (20, 80)
        gc.translate(20, 100);
        gc.rotate(-45);
        gc.setLineWidth(1);
        // Dessinez un rectangle avec la méthode fillRect() ou strokeRect()
        gc.fillRect(0, 0, 110, 10);
        gc.restore();


        //Definition du mot à deviner

        //Score à Zero
        score.setText(String.valueOf(val));


        //Initialisation du mot caché
        final StringBuilder[] motCache = {new StringBuilder()};
        for (int i = 0; i < motPredefini.length(); i++) {
            motCache[0].append("*");
        }
        //Affichage du mot caché dans un label
          mot.setText(String.valueOf(motCache[0]));
        // Créer un tableau de lettres de l'alphabet français
        String[] lettres = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        // Ajouter des boutons pour chaque lettre de l'alphabet français
        int row = 0;
        int col = 0;
        for (String i : lettres) {
            Button button = new Button(i);
            button.setOnAction(e -> {
                String letterClicked = button.getText();
                //String currentWord = mot.getText();
                StringBuilder newWord = new StringBuilder();
                //Faire appel à la fonction qu'une seule fois
                boolean functionCalled = false;
                //Nombre de defaite
                for (int j = 0; j < motPredefini.length(); j++) {
                    if (motPredefini.charAt(j) == letterClicked.charAt(0)) {
                        newWord.append(letterClicked);
                        val.getAndIncrement();
                        functionCalled = true;
                    } else {
                        newWord.append(motCache[0].charAt(j));
                    }
                }
                if (!functionCalled) {
                    handleIncorrectGuess(erreur.shortValue());
                    functionCalled = true;
                    //INcrementer le nombre d'erreur
                    erreur.getAndIncrement();
                }
                motCache[0] = newWord;
                // Vérifier si le mot a été deviné
                if (!motCache[0].toString().contains("*")) {
                    wordIsGuessed[0].set(true);
                }
                score.setText(String.valueOf(val.get()));
                mot.setText(motCache[0].toString());
                // Vérifier si le mot a été deviné pour sortir de la boucle while
                if (motPredefini.equals(newWord.toString()) ) {
                    // Sortir de la boucle en retournant ici
                    val.addAndGet(5);

                    try {
                        Thread.sleep(2000);
                        reinitialize(previousName);
                    } catch (InterruptedException o) {
                        o.printStackTrace();
                    }
                    //return;
                }
            });
            gridPane.getChildren().add(button);
            GridPane.setConstraints(button, col, row);
            col++;

            if (col == 6) {
                col = 0;
                row++;
            }
        }
    }
    public void handleIncorrectGuess(int newError) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        erreur.set(newError);
        int errors = erreur.get();

        switch (errors) {
            case 1:
                // Draw the noose
                gc.fillRect(20, 20, 20, 280);
                gc.fillRect(20, 20, 180, 10);
                gc.fillRect(120, 20, 10, 40);
                break;
            case 2:
                // Draw the head
                gc.strokeOval(100, 60, 40, 40);
                break;
            case 3:
                // Draw the body
                gc.strokeLine(120, 100, 120, 200);
                break;
            case 4:
                // Draw the right arm
                gc.strokeLine(120, 120, 160, 160);
                break;
            case 5:
                // Draw the left arm
                gc.strokeLine(120, 120, 80, 160);
                break;
            case 6:
                // Draw the right leg
                gc.strokeLine(120, 200, 160, 240);
                break;
            case 7:
                // Draw the left leg
                gc.strokeLine(120, 200, 80, 240);
                break;
            case 8:
                try {
                    Thread.sleep(2000); // Attendre 5000 millisecondes, soit 5 secondes
                    reinitialize(previousName);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
