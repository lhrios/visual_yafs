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

package visual.yafs.gui.operation.mode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import visual.yafs.common.VisualYAFSConstants;
import visual.yafs.gui.wizard.Wizard;
import visual.yafs.utils.FXUtils;

public class OperationModeSelection {
	private final Stage stage = new Stage();

	public OperationModeSelection() {
		this.stage.initStyle(StageStyle.DECORATED);
		this.stage.setResizable(false);
		this.stage.setTitle(VisualYAFSConstants.APPLICATION_NAME);

		/* Creates the scene. */
		GridPane gridPane = VisualYAFSConstants.newGridPane();

		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setPercentWidth(50);
		gridPane.getColumnConstraints().add(columnConstraints);
		columnConstraints = new ColumnConstraints();
		columnConstraints.setPercentWidth(50);
		gridPane.getColumnConstraints().add(columnConstraints);

		Label question = new Label("Which mode do you want to use?");
		GridPane.setConstraints(question, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		gridPane.getChildren().add(question);

		Image image = FXUtils.loadImage("/icons/hat_and_magic_wand.png");
		ImageView imageView = new ImageView(image);
		Button wizardMode = new Button("Wizard mode", imageView);
		wizardMode.setTooltip(new Tooltip("An step by step operation mode. It allows easy and fast sorting for common cases."));
		wizardMode.setContentDisplay(ContentDisplay.TOP);
		wizardMode.setMaxWidth(Double.MAX_VALUE);
		wizardMode.setMaxHeight(Double.MAX_VALUE);
		GridPane.setConstraints(wizardMode, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		gridPane.getChildren().add(wizardMode);

		image = FXUtils.loadImage("/icons/three_gears.png");
		imageView = new ImageView(image);
		Button advancedMode = new Button("Advanced mode\n(not implemented yet)", imageView);
		advancedMode.setTextAlignment(TextAlignment.CENTER);
		advancedMode.setTooltip(new Tooltip("An operation mode that allows arbitrary sorting. It is not implemented yet."));
		advancedMode.setContentDisplay(ContentDisplay.TOP);
		advancedMode.setMaxHeight(Double.MAX_VALUE);
		advancedMode.setMaxWidth(Double.MAX_VALUE);
		advancedMode.setDisable(true);
		GridPane.setConstraints(advancedMode, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		gridPane.getChildren().add(advancedMode);

		Scene scene = new Scene(gridPane);
		this.stage.setScene(scene);

		wizardMode.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				new Wizard();
				OperationModeSelection.this.stage.close();
			}
		});

		VisualYAFSConstants.setVisualYAFSIcon(this.stage);

		this.stage.show();
		this.stage.sizeToScene();
		FXUtils.centerOnScreen(OperationModeSelection.this.stage);
	}
}
