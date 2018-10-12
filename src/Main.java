import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import javafx.scene.control.ToggleGroup;

public class Main extends Application {
    private Image image;
    private ToggleGroup toggleGroup = new ToggleGroup();

    private Integer photoNumber = 1;
    private Integer totalNumberOfPhotos = 5;

    private String fileName;

    private boolean uploaded = false;

    ToggleButton[] toggleButtons = new ToggleButton[6];
    FileInputStream[] fileStream = new FileInputStream[6];
    Image[] images = new Image[6];

    ChoiceBox choiceBox = new ChoiceBox();

    private BorderPane border = new BorderPane();

    ImageView imageView;

    HBox buttonLayout;

    private void setupImage(int x) {
        toggleButtons[x - 1] = new ToggleButton(Integer.toString(x));
        toggleButtons[x - 1].setToggleGroup(toggleGroup);

        try {
            fileStream[x - 1] = new FileInputStream("/Users/andrewrodebaugh/IdeaProjects/ImageViewer/src/screenshot" + x + ".png");
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
        }

        images[x - 1] = new Image(fileStream[x - 1]);

        choiceBox.getItems().add(Integer.toString(x));
        choiceBox.getSelectionModel().selectFirst();

        ImageView buttonImage = new ImageView(images[x - 1]);

        buttonImage.setFitHeight(250);
        buttonImage.setFitWidth(150);
        buttonImage.setPreserveRatio(true);

        toggleButtons[x - 1].setGraphic(buttonImage);

        toggleButtons[x - 1].setOnAction(e -> {
            String str = e.getSource().toString();
            Integer numberSelected = Character.getNumericValue(str.substring(Math.max(str.length() - 2, 0)).charAt(0));

            imageView.setImage(images[numberSelected - 1]);

            choiceBox.getSelectionModel().select(x - 1);
        });

        choiceBox.setOnAction(e -> {
            imageView.setImage(images[choiceBox.getSelectionModel().getSelectedIndex()]);

            toggleButtons[choiceBox.getSelectionModel().getSelectedIndex()].setSelected(true);
        });

        buttonLayout.getChildren().add(toggleButtons[x - 1]);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Image Viewer");
        primaryStage.setResizable(false);

        FileInputStream input = new FileInputStream("/Users/andrewrodebaugh/IdeaProjects/ImageViewer/src/screenshot" + photoNumber + ".png");
        image = new Image(input);
        imageView = new ImageView(image);
        imageView.setFitHeight(2000);
        imageView.setFitWidth(1000);
        imageView.setPreserveRatio(true);

        HBox imageLayout = new HBox();
        imageLayout.getChildren().add(imageView);

        buttonLayout = new HBox();

        for (int x = 1; x < totalNumberOfPhotos + 1; x++) {
            setupImage(x);
        }

        toggleButtons[5] = new ToggleButton("+");
        toggleButtons[5].setToggleGroup(toggleGroup);
        toggleButtons[5].setMinSize(98,99);

        toggleButtons[5].setOnAction(e -> {
            if (!uploaded) {
                FileChooser.ExtensionFilter imageFilter
                        = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");

                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(imageFilter);

                fileName = new File(fileChooser.showOpenDialog(primaryStage).getAbsolutePath()).toURI().toString();

                Image openedImage = new Image(fileName);

                imageView.setImage(openedImage);

                toggleButtons[5].setText("5");

                try {
                    fileStream[5] = new FileInputStream(fileName);
                } catch (FileNotFoundException fnfe) {
                    System.out.println(fnfe.getMessage());
                }

                images[5] = new Image(fileName);

                ImageView buttonImage = new ImageView(images[5]);

                buttonImage.setFitHeight(250);
                buttonImage.setFitWidth(150);
                buttonImage.setPreserveRatio(true);

                toggleButtons[5].setGraphic(buttonImage);

                uploaded = true;
            } else {
                Image openedImage = new Image(fileName);

                imageView.setImage(openedImage);
                
                choiceBox.getSelectionModel().select(5); // Need to add choiceBox
            }
        });

        buttonLayout.getChildren().add(toggleButtons[5]);

        ScrollPane scrollPane = new ScrollPane(buttonLayout);
        scrollPane.setFitToHeight(true);

        border.setTop(choiceBox);
        border.setCenter(imageLayout);
        border.setBottom(buttonLayout);

        primaryStage.setScene(new Scene(border, 1000, 750));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
