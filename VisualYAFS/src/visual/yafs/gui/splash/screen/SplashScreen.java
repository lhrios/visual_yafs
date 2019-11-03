/*
 * Copyright 2015 Luis Henrique O. Rios
 *
 * This file is part of Visual YAFS.
 *
 * Visual YAFS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Visual YAFS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Visual YAFS.  If not, see <http://www.gnu.org/licenses/>.
 */

package visual.yafs.gui.splash.screen;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import visual.yafs.common.VisualYAFSConstants;
import visual.yafs.utils.FXUtils;

public class SplashScreen {
	private static final Font FONT = Font.font("SansSerif", FontWeight.BOLD, 12);

	private final Stage stage;

	private StringProperty progressTextProperty;
	private StringProperty versionTextProperty;

	public SplashScreen(Stage stage) {
		this.stage = stage;
		this.build(stage);
	}

	public StringProperty progressTextProperty() {
		return this.progressTextProperty;
	}

	public StringProperty versionTextProperty() {
		return this.versionTextProperty;
	}

	public void close() {
		this.stage.close();
	}

	private void build(Stage primaryStage) {
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setResizable(false);
		primaryStage.setTitle(VisualYAFSConstants.APPLICATION_NAME);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				event.consume();
			}
		});
		primaryStage.setOnShowing(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				FXUtils.centerOnScreen(primaryStage);
			}
		});

		/* Creates the scene. */
		Group group = new Group();

		Image image = FXUtils.loadImage("/images/splash_screen.png");
		ImageView imageView = new ImageView(image);
		group.getChildren().add(imageView);

		Dimension2D availableArea = new Dimension2D(image.getWidth(), image.getHeight());
		this.createAndInsertProgressBar(group, availableArea);
		this.createAndInsertProgressText(group, availableArea);
		this.createAndInsertVersionText(group, availableArea);

		Scene scene = new Scene(group);
		primaryStage.setScene(scene);

		VisualYAFSConstants.setVisualYAFSIcon(primaryStage);

		/* Adjust the size. */
		primaryStage.setMaxWidth(image.getWidth());
		primaryStage.setMaxHeight(image.getHeight());

		primaryStage.setWidth(image.getWidth());
		primaryStage.setHeight(image.getHeight());
	}

	private void createAndInsertProgressBar(Group parent, Dimension2D availableArea) {
		ProgressBar progressBar = new ProgressBar();
		progressBar.setProgress(-1);

		progressBar.minWidthProperty().set(availableArea.getWidth() * 0.70);
		double finalX = (availableArea.getWidth() - progressBar.minWidthProperty().get()) / 2;
		double finalY = 220;

		progressBar.layoutXProperty().set(finalX - progressBar.getLayoutBounds().getMinX());
		progressBar.layoutYProperty().set(finalY - progressBar.getLayoutBounds().getMinY());
		parent.getChildren().add(progressBar);

	}

	private void createAndInsertProgressText(Group parent, Dimension2D availableArea) {
		Text text = new Text();

		text.setTextAlignment(TextAlignment.CENTER);
		text.setWrappingWidth(availableArea.getWidth() * 0.70);
		text.setTextOrigin(VPos.BOTTOM);
		text.setFill(Color.WHITE);
		text.setFont(FONT);

		double finalX = (availableArea.getWidth() - text.getWrappingWidth()) / 2;
		double finalY = 200;

		text.layoutXProperty().set(finalX - text.getLayoutBounds().getMinX());
		text.layoutYProperty().set(finalY - text.getLayoutBounds().getMinY());
		parent.getChildren().add(text);

		this.progressTextProperty = text.textProperty();
	}

	private void createAndInsertVersionText(Group parent, Dimension2D availableArea) {
		Text text = new Text();

		text.setTextAlignment(TextAlignment.RIGHT);
		text.setWrappingWidth(availableArea.getWidth() - 10);
		text.setTextOrigin(VPos.BOTTOM);
		text.setFill(Color.WHITE);
		text.setFont(FONT);

		double finalX = (availableArea.getWidth() - text.getWrappingWidth()) / 2;
		double finalY = 145;

		text.layoutXProperty().set(finalX - text.getLayoutBounds().getMinX());
		text.layoutYProperty().set(finalY - text.getLayoutBounds().getMinY());
		parent.getChildren().add(text);

		this.versionTextProperty = text.textProperty();
	}
}
