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

package visual.yafs.executor;

public class YAFSExecutionResult {
	private String stdout;
	private String stderr;
	private Integer exitStatus;

	public Integer getExitStatus() {
		return this.exitStatus;
	}

	public void setExitStatus(Integer exitStatus) {
		this.exitStatus = exitStatus;
	}

	private Throwable throwable;

	public String getStdout() {
		return this.stdout;
	}

	public void setStdout(String stdout) {
		this.stdout = stdout;
	}

	public String getStderr() {
		return this.stderr;
	}

	public void setStderr(String stderr) {
		this.stderr = stderr;
	}

	public Throwable getThrowable() {
		return this.throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public boolean hasSucceeded() {
		return this.getExitStatus() != null && this.getExitStatus() == 0 && this.getThrowable() == null;
	}
}
