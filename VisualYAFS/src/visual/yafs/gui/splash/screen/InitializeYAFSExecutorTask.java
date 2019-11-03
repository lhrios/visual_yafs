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

import java.nio.file.Path;

import visual.yafs.application.ApplicationContext.Key;
import visual.yafs.application.VisualYAFSApplication;
import visual.yafs.executor.YAFSExecutor;
import visual.yafs.gui.common.VisualYAFSTask;
import visual.yafs.gui.operation.mode.OperationModeSelection;

public class InitializeYAFSExecutorTask extends VisualYAFSTask<Void> {
	private final SplashScreen splashScreen;

	public InitializeYAFSExecutorTask(SplashScreen splashScreen) {
		this.splashScreen = splashScreen;
	}

	@Override
	protected Void call() throws Exception {
		this.updateMessage("Preparing YAFS...");
		Path visualYAFStemporaryFolder = VisualYAFSApplication.getInstance().getTemporaryFolder();
		YAFSExecutor yafsExecutor = new YAFSExecutor(visualYAFStemporaryFolder);
		yafsExecutor.prepareAndCheckYAFS();
		Thread.sleep(750); /* It uses sleep in order to permit splash screen visualization. */

		this.updateMessage("Loading the interface...");
		Thread.sleep(750);

		this.updateMessage(null);

		VisualYAFSApplication.getInstance().getApplicationContext().setAttribute(Key.YAFS_EXECUTOR, yafsExecutor);

		return null;
	}

	@Override
	protected void succeeded() {
		this.splashScreen.close();

		new OperationModeSelection();
	}
}
