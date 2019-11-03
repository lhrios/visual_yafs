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

package visual.yafs.utils;

import java.io.IOException;
import java.io.InputStream;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import visual.yafs.gui.splash.screen.SplashScreen;

public interface FXUtils {

	public static void centerOnScreen(Stage stage) {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double x = (primaryScreenBounds.getWidth() - stage.getWidth()) / 2;
		double y = (primaryScreenBounds.getHeight() - stage.getHeight()) / 2;
		stage.setX(x);
		stage.setY(y);
	}

	public static Image loadImage(String resource) {
		try (InputStream inputStream = SplashScreen.class.getResourceAsStream(resource)) {
			Image image = new Image(inputStream);
			return image;

		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void setClipboardContent(String content) {
		ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(content);
		Clipboard.getSystemClipboard().setContent(clipboardContent);
	}
}
