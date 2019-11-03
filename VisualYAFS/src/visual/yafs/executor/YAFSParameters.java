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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import visual.yafs.utils.SystemUtils;
import visual.yafs.utils.SystemUtils.OperatingSystem;

public class YAFSParameters {
	private Path file;
	private boolean read;
	private Path device;

	public static YAFSParameters parametersToExecuteHelp() {
		YAFSParameters parameters = new YAFSParameters();
		return parameters;
	}

	public static YAFSParameters parametersToReadSystemDirectoryTree(Path device, Path file) {
		YAFSParameters parameters = new YAFSParameters();
		parameters.read = true;
		parameters.device = device;
		parameters.file = file;
		return parameters;
	}

	public static YAFSParameters parametersToWriteSystemDirectoryTree(Path device, Path file) {
		YAFSParameters parameters = new YAFSParameters();
		parameters.read = false;
		parameters.device = device;
		parameters.file = file;
		return parameters;
	}

	/* default */List<String> toList() {
		List<String> list = new ArrayList<>();
		if (this.device != null) {
			list.add("-d");
			String deviceAsString = this.device.toString();
			if (SystemUtils.HOST_OPERATING_SYSTEM == OperatingSystem.WINDOWS) {
				char lastChar = deviceAsString.charAt(deviceAsString.length() - 1);
				if (lastChar == '\\' || lastChar == '/') {
					deviceAsString = deviceAsString.substring(0, deviceAsString.length() - 1);
				}
			}
			list.add(deviceAsString);

			list.add(this.read ? "-r" : "-w");

			list.add("-f");
			list.add(this.file.toString());

		} else {
			list.add("-h");
		}

		return list;
	}
}
