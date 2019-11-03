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

package visual.yafs.gui.common;

import javafx.concurrent.Task;
import visual.yafs.executor.YAFSExecutionException;
import visual.yafs.gui.exception.UnexpectedThrowableStage;
import visual.yafs.gui.yafs.YAFSExecutionResultStage;

public abstract class VisualYAFSTask<V> extends Task<V> {
	@Override
	protected void failed() {
		Throwable throwable = this.getException();
		if (throwable instanceof YAFSExecutionException) {
			YAFSExecutionException yafsExecutionException = (YAFSExecutionException) throwable;
			YAFSExecutionResultStage.createDialogAndShowExecutionResultAndExit(yafsExecutionException.getExecutionResult());

		} else {
			UnexpectedThrowableStage.createDialogAndShowThrowableAndExit(throwable);
		}
	}
}
