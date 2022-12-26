package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    Pane titlePane;
    @FXML
    private RadioButton fillRB;
    @FXML
    private ColorPicker colorPick;
    @FXML
    private Canvas TheCanvas, canvasGo;
    @FXML
    private Slider sizeSlider;
    @FXML
    private Label sizeLabel;


    private GraphicsContext gcB, gcF;
    private boolean drawLine = false, drawOval = false, drawRectangle = false, freeDesign = true, freeErase = false;
    double startX, startY, lastX, lastY, oldX, oldY;
    private double x, y;

    @FXML
    private void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    private void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gcB = TheCanvas.getGraphicsContext2D();
        gcF = canvasGo.getGraphicsContext2D();
        sizeSlider.setMin(1);
        sizeSlider.setMax(50);
        sizeLabel.textProperty().bind(Bindings.format(
                "%.0f", sizeSlider.valueProperty()));
        DoubleProperty value = sizeSlider.valueProperty();

    }

    @FXML
    private void onMousePressedListener(MouseEvent event) {
        this.startX = event.getX();
        this.startY = event.getY();
        this.oldX = event.getX();
        this.oldY = event.getY();
    }

    @FXML
    private void onMouseDraggedListener(MouseEvent event) {
        this.lastX = event.getX();
        this.lastY = event.getY();

        if(drawRectangle)
            drawRectEffect();
        if(drawOval)
            drawOvalEffect();
        if(drawLine)
            drawLineEffect();
        if(freeDesign)
            freeDrawing();
        if (freeErase)
            freeErase();
    }

    @FXML
    private void onMouseReleaseListener(MouseEvent event) {
        if(drawRectangle)
            drawRect();
        if(drawOval)
            drawOval();
        if(drawLine)
            drawLine();
    }

    @FXML
    private void onMouseExitedListener(MouseEvent event) {
    }

    private void drawOval() {
        double dx = lastX - startX;
        double dy = lastY - startY;
        gcB.setLineWidth(sizeSlider.getValue());

        if(fillRB.isSelected()) {
            gcB.setFill(colorPick.getValue());
            gcB.fillOval(startX, startY, dx, dy);
        } else {
            gcB.setStroke(colorPick.getValue());
            gcB.strokeOval(startX, startY, dx, dy);
        }
    }

    private void drawRect() {
        double dx = lastX - startX;
        double dy = lastY - startY;
        gcB.setLineWidth(sizeSlider.getValue());

        if(fillRB.isSelected()) {
            gcB.setFill(colorPick.getValue());
            gcB.fillRect(startX, startY, dx, dy);
        } else {
            gcB.setStroke(colorPick.getValue());
            gcB.strokeRect(startX, startY, dx, dy);
        }
    }

    private void drawLine() {
        gcB.setLineWidth(sizeSlider.getValue());
        gcB.setStroke(colorPick.getValue());
        gcB.strokeLine(startX, startY, lastX, lastY);
    }

    private void freeDrawing() {
        gcB.setLineWidth(sizeSlider.getValue());
        gcB.setStroke(colorPick.getValue());
        gcB.strokeLine(oldX, oldY, lastX, lastY);
        oldX = lastX;
        oldY = lastY;
    }

    private void freeErase() {
        gcB.setLineWidth(sizeSlider.getValue());
        gcB.setStroke(Color.WHITE);
        gcB.strokeLine(oldX, oldY, lastX, lastY);
        oldX = lastX;
        oldY = lastY;
    }

    private void drawOvalEffect() {
        double dx = lastX - startX;
        double dy = lastY - startY;
        gcF.setLineWidth(sizeSlider.getValue());

        if(fillRB.isSelected()) {
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setFill(colorPick.getValue());
            gcF.fillOval(startX, startY, dx, dy);
        } else {
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setStroke(colorPick.getValue());
            gcF.strokeOval(startX, startY, dx, dy);
        }
    }

    private void drawRectEffect() {
        double dx = lastX - startX;
        double dy = lastY - startY;
        gcF.setLineWidth(sizeSlider.getValue());

        if(fillRB.isSelected()) {
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setFill(colorPick.getValue());
            gcF.fillRect(startX, startY, dx, dy);
        } else {
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setStroke(colorPick.getValue());
            gcF.strokeRect(startX, startY, dx, dy );
        }
    }

    private void drawLineEffect() {
        gcF.setLineWidth(sizeSlider.getValue());
        gcF.setStroke(colorPick.getValue());
        gcF.clearRect(0, 0, canvasGo.getWidth() , canvasGo.getHeight());
        gcF.strokeLine(startX, startY, lastX, lastY);
    }

    @FXML
    private void clearCanvas(ActionEvent event) {
        gcB.clearRect(0, 0, TheCanvas.getWidth(), TheCanvas.getHeight());
        gcF.clearRect(0, 0, TheCanvas.getWidth(), TheCanvas.getHeight());
    }

    @FXML
    private void setOvalAsCurrentShape(ActionEvent event) {
        drawLine = false;
        drawOval = true;
        drawRectangle = false;
        freeDesign = false;
        freeErase = false;
    }

    @FXML
    private void setLineAsCurrentShape(ActionEvent event) {
        drawLine = true;
        drawOval = false;
        drawRectangle = false;
        freeDesign = false;
        freeErase = false;
    }
    @FXML
    private void setRectangleAsCurrentShape(ActionEvent event) {
        drawLine = false;
        drawOval = false;
        freeDesign = false;
        drawRectangle = true;
        freeErase = false;
    }

    @FXML
    private void setFreeErase(ActionEvent event) {
        drawLine = false;
        drawOval = false;
        drawRectangle = false;
        freeDesign = false;
        freeErase = true;
    }

    @FXML
    private void setFreeDesign(ActionEvent event) {
        drawLine = false;
        drawOval = false;
        drawRectangle = false;
        freeDesign = true;
        freeErase = false;
    }

    @FXML
    private void slideLineWidth(ActionEvent event) {
        sizeLabel.textProperty().bind(Bindings.format(
                "%.0f", sizeSlider.valueProperty()));
    }

}
