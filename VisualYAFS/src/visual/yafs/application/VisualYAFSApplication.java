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

package visual.yafs.application;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import visual.yafs.application.ApplicationContext.Key;
import visual.yafs.common.ApplicationSingleInstanceManager;
import visual.yafs.common.VisualYAFSConstants;
import visual.yafs.gui.exception.UnexpectedThrowableStage;
import visual.yafs.gui.splash.screen.InitializeYAFSExecutorTask;
import visual.yafs.gui.splash.screen.SplashScreen;
import visual.yafs.utils.SystemUtils;

public class VisualYAFSApplication extends Application {
	public static void main(String[] args) {
		VisualYAFSApplication.launch(args);
	}

	public static VisualYAFSApplication getInstance() {
		return application;
	}

	@Override
	public void init() throws Exception {
		application = this;

		this.getApplicationContext().setAttribute(Key.TASK_EXECUTOR, new ThreadPoolExecutor(0, 2, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>()));
		this.getApplicationContext().setAttribute(Key.APPLICATION_SINGLE_INSTANCE_MANAGER,
				new ApplicationSingleInstanceManager(VisualYAFSApplication.getInstance().getTemporaryFolder(), VisualYAFSConstants.APPLICATION_FILE_TO_LOCK));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable throwable) {
				UnexpectedThrowableStage.createDialogAndShowThrowableAndExit(throwable);
			}
		});

		ApplicationSingleInstanceManager applicationSingleInstanceManager = this.getApplicationContext().getAttribute(Key.APPLICATION_SINGLE_INSTANCE_MANAGER);
		if (applicationSingleInstanceManager.thereAreOtherIntances()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(VisualYAFSConstants.APPLICATION_NAME);
			alert.setHeaderText("Only one instance of Visual YAFS is allowed to be executed at a time.");
			VisualYAFSConstants.setVisualYAFSIcon(alert);

			alert.showAndWait();

		} else {
			SplashScreen splashScreen = new SplashScreen(primaryStage);
			splashScreen.versionTextProperty().set(String.format("Version: %s", VisualYAFSConstants.VISUAL_YAFS_VERSION));
			primaryStage.show();
			primaryStage.toFront();

			InitializeYAFSExecutorTask initializeYAFSTask = new InitializeYAFSExecutorTask(splashScreen);
			splashScreen.progressTextProperty().bind(initializeYAFSTask.messageProperty());
			Executor executor = this.getApplicationContext().getAttribute(Key.TASK_EXECUTOR);
			executor.execute(initializeYAFSTask);
		}
	}

	@Override
	public void stop() throws Exception {
		System.exit(0);
	}

	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	public Path getTemporaryFolder() {
		return SystemUtils.getTemporaryFolder().resolve(VisualYAFSConstants.TEMPORARY_SUBFOLDER).resolve(VisualYAFSConstants.VISUAL_YAFS_VERSION);
	}

	private static VisualYAFSApplication application;

	private final ApplicationContext applicationContext = new ApplicationContext();
}
