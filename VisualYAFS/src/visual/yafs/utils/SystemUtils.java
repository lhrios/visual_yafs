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

import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemUtils {
	public static enum OperatingSystem {
		//@formatter:off
		WINDOWS,
		LINUX,
		MAC_OS_X
		//@formatter:on
	}

	public static final OperatingSystem HOST_OPERATING_SYSTEM;

	static {
		OperatingSystem hostOperatingSystem = null;
		String operatingSystemName = getOperatingSystemName().toLowerCase().replace(' ', '_');
		for (OperatingSystem operatingSystem : OperatingSystem.values()) {
			if (operatingSystemName.startsWith(operatingSystem.name().toLowerCase())) {
				hostOperatingSystem = operatingSystem;
				break;
			}
		}

		HOST_OPERATING_SYSTEM = hostOperatingSystem;
	}

	public static String getOperatingSystemName() {
		return System.getProperty("os.name");
	}

	public static Path getTemporaryFolder() {
		return Paths.get(System.getProperty("java.io.tmpdir"));
	}
}
