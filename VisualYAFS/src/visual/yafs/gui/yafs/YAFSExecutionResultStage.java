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

package visual.yafs.gui.yafs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import visual.yafs.common.VisualYAFSConstants;
import visual.yafs.executor.YAFSExecutionResult;
import visual.yafs.utils.ExceptionUtils;
import visual.yafs.utils.FXUtils;
import visual.yafs.utils.StringUtils;

public class YAFSExecutionResultStage {
	public static void createDialogAndShowExecutionResultAndExit(final YAFSExecutionResult executionResult) {
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(VisualYAFSConstants.APPLICATION_NAME);
		alert.setHeaderText("YAFS execution finished with an unexpected result. The program will be closed.");

		GridPane contentPane = VisualYAFSConstants.newGridPane();

		final Label exitStatusLabel = new Label(String.format("Exit status: %s", executionResult.getExitStatus()));
		GridPane.setConstraints(exitStatusLabel, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
		contentPane.getChildren().add(exitStatusLabel);

		Button copyToClipboard = new Button("Copy to clipboard");
		GridPane.setConstraints(copyToClipboard, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);
		contentPane.getChildren().add(copyToClipboard);

		TabPane tabPane = new TabPane();
		if (executionResult.getThrowable() != null) {
			tabPane.getTabs().add(createTab("Exception", ExceptionUtils.getStackTrace(executionResult.getThrowable())));
		}
		tabPane.getTabs().add(createTab("stderr", executionResult.getStderr()));
		tabPane.getTabs().add(createTab("stdout", executionResult.getStdout()));

		copyToClipboard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(alert.getHeaderText()).append('\n');

				stringBuilder.append('\n').append(exitStatusLabel.getText()).append('\n');

				stringBuilder.append('\n').append("stderr:").append('\n');
				stringBuilder.append(executionResult.getStderr()).append('\n');

				stringBuilder.append('\n').append("stdout:").append('\n');
				stringBuilder.append(executionResult.getStdout()).append('\n');

				if (executionResult.getThrowable() != null) {
					stringBuilder.append('\n').append("Exception:").append('\n');
					stringBuilder.append(ExceptionUtils.getStackTrace(executionResult.getThrowable()));
				}

				String content = stringBuilder.toString().replace(StringUtils.LINE_SEPARATOR, "\n");
				FXUtils.setClipboardContent(content);
			}
		});

		GridPane.setConstraints(tabPane, 0, 1, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		contentPane.getChildren().add(tabPane);

		alert.getDialogPane().setExpandableContent(contentPane);

		VisualYAFSConstants.setVisualYAFSIcon(alert);

		alert.showAndWait();
		System.exit(1);
	}

	private static Tab createTab(String title, String text) {
		Tab tab = new Tab();
		tab.setClosable(false);
		tab.setText(title);

		TextArea textArea = new TextArea(text);
		textArea.setEditable(false);
		tab.setContent(textArea);

		return tab;
	}
}
