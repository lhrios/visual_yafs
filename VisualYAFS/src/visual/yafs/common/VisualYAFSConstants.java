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

package visual.yafs.common;

import javafx.geometry.Insets;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import visual.yafs.utils.FXUtils;

public interface VisualYAFSConstants {
	public static final String VISUAL_YAFS_VERSION = "2015_05_18";

	public static final String EXPECTED_YAFS_VERSION = "2015_07_27";

	public static final String TEMPORARY_SUBFOLDER = "visual_yafs";

	public static final String APPLICATION_FILE_TO_LOCK = "lock";

	public static final String APPLICATION_NAME = "Visual YAFS";

	/* Constants and methods related with the GUI. */
	public static Insets INSETS = new Insets(5, 5, 5, 5);

	public static GridPane newGridPane() {
		GridPane gridPane = new GridPane();
		gridPane.setPadding(INSETS);
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		return gridPane;
	}

	public static void setVisualYAFSIcon(Stage stage) {
		stage.getIcons().add(FXUtils.loadImage("/icons/visual_yafs_logo_128.png"));
	}

	public static void setVisualYAFSIcon(Dialog<?> dialog) {
		Window window = dialog.getDialogPane().getScene().getWindow();
		if (window instanceof Stage) {
			setVisualYAFSIcon((Stage) window);
		}
	}
}
