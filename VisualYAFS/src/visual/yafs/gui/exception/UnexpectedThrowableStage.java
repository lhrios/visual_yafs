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

package visual.yafs.gui.exception;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import visual.yafs.common.VisualYAFSConstants;
import visual.yafs.utils.ExceptionUtils;
import visual.yafs.utils.FXUtils;
import visual.yafs.utils.StringUtils;

public class UnexpectedThrowableStage {
	public static void createDialogAndShowThrowableAndExit(final Throwable throwable) {
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(VisualYAFSConstants.APPLICATION_NAME);
		alert.setHeaderText("An unexpected error has occurred. The program will be closed.");
		VisualYAFSConstants.setVisualYAFSIcon(alert);

		GridPane contentPane = VisualYAFSConstants.newGridPane();
		contentPane.setMaxWidth(Double.MAX_VALUE);

		Button copyToClipboard = new Button("Copy to clipboard");
		GridPane.setConstraints(copyToClipboard, 0, 0, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
		contentPane.getChildren().add(copyToClipboard);

		copyToClipboard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(ExceptionUtils.getStackTrace(throwable));

				String content = stringBuilder.toString().replace(StringUtils.LINE_SEPARATOR, "\n");
				FXUtils.setClipboardContent(content);
			}
		});

		TextArea textArea = new TextArea(ExceptionUtils.getStackTrace(throwable));
		textArea.setEditable(false);
		GridPane.setConstraints(textArea, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
		contentPane.getChildren().add(textArea);

		alert.getDialogPane().setExpandableContent(contentPane);

		alert.showAndWait();
		System.exit(1);
	}
}
