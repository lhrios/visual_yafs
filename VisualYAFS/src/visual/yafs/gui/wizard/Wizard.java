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

package visual.yafs.gui.wizard;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import visual.yafs.application.ApplicationContext.Key;
import visual.yafs.application.VisualYAFSApplication;
import visual.yafs.common.VisualYAFSConstants;
import visual.yafs.utils.FXUtils;
import visual.yafs.utils.SystemUtils;
import visual.yafs.utils.SystemUtils.OperatingSystem;

public class Wizard {
	private final Stage stage = new Stage();

	private final GridPane buttonsPane = VisualYAFSConstants.newGridPane();
	private final GridPane stepPane = VisualYAFSConstants.newGridPane();
	private final GridPane stepsPane = VisualYAFSConstants.newGridPane();

	private final Button next = new Button("Next");
	private final Button back = new Button("Back");
	private final Button executeYAFS = new Button("Execute YAFS");
	private final Button close = new Button("Close");

	private WizardStep currentStep = WizardStep.values()[0];
	private final Label stepDescription = new Label();
	private final Label selectedDeviceLabel = new Label();

	/* Step 1: */
	private SimpleObjectProperty<Path> deviceProperty = new SimpleObjectProperty<>();
	private final Button choose = new Button("Choose...");

	/* Step 2: */
	private SortCriterion sortCriterion;
	private final ToggleGroup sortCriteriaToggleGroup = new ToggleGroup();

	/* Step 4: */
	private final BooleanProperty executingYAFS = new SimpleBooleanProperty();
	private final ProgressBar progressBar = new ProgressBar();
	private final Label successLabel = new Label();

	public Wizard() {
		this.stage.initStyle(StageStyle.DECORATED);
		this.stage.setResizable(false);
		this.stage.setTitle(VisualYAFSConstants.APPLICATION_NAME);
		this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (Wizard.this.executingYAFS.get()) {
					event.consume();
				}
			}
		});

		GridPane gridPane = VisualYAFSConstants.newGridPane();

		ColumnConstraints columnConstraints = new ColumnConstraints(150);
		gridPane.getColumnConstraints().add(columnConstraints);

		GridPane.setConstraints(this.stepsPane, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		gridPane.getChildren().add(this.stepsPane);

		Separator separator = new Separator(Orientation.VERTICAL);
		GridPane.setConstraints(separator, 1, 0, 1, 1);
		gridPane.getChildren().add(separator);

		GridPane.setConstraints(this.stepPane, 2, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		gridPane.getChildren().add(this.stepPane);

		GridPane.setConstraints(this.buttonsPane, 0, 1, 3, 1);
		gridPane.getChildren().add(this.buttonsPane);

		this.prepareButtonsPane();
		this.prepareStepPane();
		this.prepareStepsPane();

		Scene scene = new Scene(gridPane);
		this.stage.setScene(scene);

		VisualYAFSConstants.setVisualYAFSIcon(this.stage);

		/* Adjust the size. */
		this.stage.setMaxWidth(700);
		this.stage.setMaxHeight(480);

		this.stage.setWidth(700);
		this.stage.setHeight(480);

		/* Prepare the components. */
		this.sortCriteriaToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (newValue != null) {
					Wizard.this.sortCriterion = (SortCriterion) newValue.getUserData();
				}
			}
		});

		this.choose.setOnAction(new EventHandler<ActionEvent>() {
			final String CHOOSER_TITLE = "Please, select a device whose file system type is FAT16 or FAT32.";

			@Override
			public void handle(ActionEvent event) {
				File device;
				if (SystemUtils.HOST_OPERATING_SYSTEM == OperatingSystem.WINDOWS) {
					DirectoryChooser directoryChooser = new DirectoryChooser();
					directoryChooser.setTitle(this.CHOOSER_TITLE);
					device = directoryChooser.showDialog(Wizard.this.stage);

				} else {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle(this.CHOOSER_TITLE);
					device = fileChooser.showOpenDialog(Wizard.this.stage);
				}

				Wizard.this.deviceProperty.setValue(device == null ? null : device.toPath());
			}
		});

		this.next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				assert Wizard.this.currentStep.ordinal() + 1 < WizardStep.values().length;

				Wizard.this.currentStep = WizardStep.values()[Wizard.this.currentStep.ordinal() + 1];
				Wizard.this.prepareButtonsPane();
				Wizard.this.prepareStepPane();
				Wizard.this.prepareStepsPane();
			}
		});

		this.back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				assert Wizard.this.currentStep.ordinal() - 1 >= 0;

				Wizard.this.currentStep = WizardStep.values()[Wizard.this.currentStep.ordinal() - 1];
				Wizard.this.prepareButtonsPane();
				Wizard.this.prepareStepPane();
				Wizard.this.prepareStepsPane();
			}
		});

		this.executeYAFS.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				assert Wizard.this.currentStep == WizardStep.CONFIRMATION;

				Wizard.this.executingYAFS.set(true);
				Wizard.this.currentStep = WizardStep.YAFS_EXECUTION;
				Wizard.this.prepareButtonsPane();
				Wizard.this.prepareStepPane();
				Wizard.this.prepareStepsPane();

				/* Prepare and run the task. */
				Executor executor = VisualYAFSApplication.getInstance().getApplicationContext().getAttribute(Key.TASK_EXECUTOR);
				ExecuteYAFSTask executeYAFSTask = new ExecuteYAFSTask(Wizard.this.deviceProperty.get(), Wizard.this.sortCriterion);
				executeYAFSTask.stateProperty().addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
						if (newValue == State.SUCCEEDED) {
							Wizard.this.executingYAFS.set(false);

							int columnIndex = GridPane.getColumnIndex(Wizard.this.stepDescription);
							int rowIndex = GridPane.getRowIndex(Wizard.this.stepDescription);
							int columnSpan = GridPane.getColumnSpan(Wizard.this.stepDescription);
							int rowSpan = GridPane.getRowSpan(Wizard.this.stepDescription);
							GridPane.setConstraints(Wizard.this.successLabel, columnIndex, rowIndex, columnSpan, rowSpan, HPos.CENTER, VPos.CENTER, Priority.ALWAYS,
									Priority.ALWAYS);

							Wizard.this.stepPane.getChildren().remove(Wizard.this.stepDescription);
							Wizard.this.stepPane.getChildren().remove(Wizard.this.progressBar);
							Wizard.this.stepPane.getChildren().add(Wizard.this.successLabel);
						}
						/* If there was a failure, the application will be closed. */
					}
				});
				executor.execute(executeYAFSTask);
			}
		});

		ImageView success = new ImageView(FXUtils.loadImage("/icons/success_32.png"));
		this.successLabel.setGraphic(success);
		this.successLabel.setText("The operation has been executed successfully.");
		Font font = this.successLabel.getFont();
		this.successLabel.setFont(Font.font(font.getFamily(), FontWeight.BOLD, font.getSize() * 1.1));

		this.close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				assert Wizard.this.currentStep == WizardStep.YAFS_EXECUTION;

				Wizard.this.stage.close();
			}
		});

		this.stage.show();
	}

	private void prepareStepsPane() {
		this.stepsPane.getChildren().clear();

		for (int i = 0; i < WizardStep.values().length; i++) {
			WizardStep wizardStep = WizardStep.values()[i];

			Label stepLabel = new Label(String.format("%d. %s", i + 1, wizardStep.getDescription()));
			stepLabel.setWrapText(true);
			stepLabel.setMaxWidth(Double.MAX_VALUE);
			if (wizardStep == this.currentStep) {
				Font font = stepLabel.getFont();
				stepLabel.setFont(Font.font(font.getFamily(), FontWeight.BOLD, font.getSize()));
			}

			GridPane.setConstraints(stepLabel, 0, i, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
			this.stepsPane.getChildren().add(stepLabel);
		}
	}

	private void prepareStepPane() {
		this.stepPane.getChildren().clear();

		int rowIndex = 0;
		Label title = new Label(this.currentStep.getDescription());
		Font font = title.getFont();
		title.setUnderline(true);
		title.setFont(Font.font(font.getFamily(), FontWeight.BOLD, font.getSize() * 1.5));
		GridPane.setConstraints(title, 0, rowIndex++, 1, 1);
		this.stepPane.getChildren().add(title);

		GridPane.setConstraints(this.stepDescription, 0, rowIndex++, 1, 1);
		this.stepPane.getChildren().add(this.stepDescription);
		this.stepDescription.setTextAlignment(TextAlignment.JUSTIFY);
		this.stepDescription.setWrapText(true);

		switch (this.currentStep) {
			case DEVICE_SELECTION: {
				//@formatter:off
				this.stepDescription.setText("\n" +
						"Please, select a device whose file system type is FAT16 or FAT32. The device must cointain the entries (files and/or folders) that will be sorted." +
						"\n" +
						"\n" +
						"Remember that the device must not be busy. " +
						"Otherwise, operation can fail and/or device's file system can be corrupted." +
						"\n" +
						"\n" +
						"Putting it in another way, if the operating system belongs to Windows family, none of device's files and/or folders must be opened. " +
						"If operating system belongs to Unix family, the device must not be mounted." +
						"\n" +
						"\n");
				//@formatter:on

				this.selectedDeviceLabel.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
					@Override
					public String call() throws Exception {
						return String.format("Selected device: %s", Wizard.this.deviceProperty.getValue() == null ? "" : Wizard.this.deviceProperty.getValue());
					}
				}, this.deviceProperty));
				this.selectedDeviceLabel.setTextAlignment(TextAlignment.JUSTIFY);
				this.selectedDeviceLabel.setWrapText(true);
				GridPane.setConstraints(this.selectedDeviceLabel, 0, rowIndex++, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
				this.stepPane.getChildren().add(this.selectedDeviceLabel);

				GridPane.setConstraints(this.choose, 0, rowIndex++, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
				this.stepPane.getChildren().add(this.choose);
			}
			break;

			case SORT_CRITERION_SELECTION: {
				//@formatter:off
				this.stepDescription.setText("\n" +
						"Please, choose how the device's entries (files and/or folders) will be sorted." +
						"\n" +
						"\n");
				//@formatter:on

				this.sortCriteriaToggleGroup.getToggles().clear();
				for (SortCriterion sortCriterion : SortCriterion.values()) {
					RadioButton radioButton = new RadioButton(sortCriterion.getDescription());
					radioButton.setUserData(sortCriterion);
					radioButton.setToggleGroup(this.sortCriteriaToggleGroup);
					radioButton.setSelected(sortCriterion == this.sortCriterion);
					GridPane.setConstraints(radioButton, 0, rowIndex++, 1, 1);
					this.stepPane.getChildren().add(radioButton);
				}
			}
			break;

			case CONFIRMATION: {
				//@formatter:off
				this.stepDescription.setText("\n" +
						"Please, confirm or change the execution plan created based on previous steps." +
						"\n" +
						"\n");
				//@formatter:on

				GridPane.setConstraints(this.selectedDeviceLabel, 0, rowIndex++, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
				this.stepPane.getChildren().add(this.selectedDeviceLabel);

				Label sortCriterionLabel = new Label();
				sortCriterionLabel.setWrapText(true);
				sortCriterionLabel.setTextAlignment(TextAlignment.JUSTIFY);
				GridPane.setConstraints(sortCriterionLabel, 0, rowIndex++, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);
				sortCriterionLabel.setText(String.format("The device's entries will be sorted using the following criterion: %s", this.sortCriterion.getDescription()));
				this.stepPane.getChildren().add(sortCriterionLabel);
			}
			break;

			case YAFS_EXECUTION: {
				//@formatter:off
				this.stepDescription.setText("\n" +
						"Please, wait while YAFS is performing the requested task." +
						"\n" +
						"\n");
				//@formatter:on

				this.progressBar.setProgress(-1);
				this.progressBar.setMaxWidth(Double.MAX_VALUE);

				GridPane.setConstraints(this.progressBar, 0, rowIndex++, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
				this.stepPane.getChildren().add(this.progressBar);
			}
			break;
		}
	}

	private void prepareButtonsPane() {
		this.buttonsPane.getChildren().clear();

		List<Button> buttons = new ArrayList<>();

		switch (this.currentStep) {
			case DEVICE_SELECTION: {
				buttons.add(this.back);
				buttons.add(this.next);
				buttons.add(this.executeYAFS);

				this.back.disableProperty().unbind();
				this.back.setDisable(true);

				this.next.disableProperty().unbind();
				this.next.disableProperty().bind(Bindings.isNull(this.deviceProperty));

				this.executeYAFS.disableProperty().unbind();
				this.executeYAFS.setDisable(true);
			}
			break;

			case SORT_CRITERION_SELECTION: {
				buttons.add(this.back);
				buttons.add(this.next);
				buttons.add(this.executeYAFS);

				this.back.disableProperty().unbind();
				this.back.setDisable(false);

				this.next.disableProperty().unbind();
				this.next.disableProperty().bind(Bindings.isNull(this.sortCriteriaToggleGroup.selectedToggleProperty()));

				this.executeYAFS.disableProperty().unbind();
				this.executeYAFS.setDisable(true);
			}
			break;

			case CONFIRMATION: {
				buttons.add(this.back);
				buttons.add(this.next);
				buttons.add(this.executeYAFS);

				this.back.disableProperty().unbind();
				this.back.setDisable(false);

				this.next.disableProperty().unbind();
				this.next.setDisable(true);

				this.executeYAFS.disableProperty().unbind();
				this.executeYAFS.setDisable(false);
			}
			break;

			case YAFS_EXECUTION: {
				buttons.add(this.close);

				this.close.disableProperty().unbind();
				this.close.disableProperty().bind(this.executingYAFS);
			}
			break;
		}

		int columnIndex = 0;
		for (Button button : buttons) {
			/* Is it the first ? */
			if (columnIndex == 0) {
				GridPane.setConstraints(button, columnIndex, 0, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER);

			} else {
				GridPane.setConstraints(button, columnIndex, 0, 1, 1);
			}

			this.buttonsPane.getChildren().add(button);
			columnIndex++;
		}
	}
}
