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

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ApplicationSingleInstanceManager {
	private FileLock fileLock;

	public ApplicationSingleInstanceManager(Path folder, String fileToLock) {
		try {
			Files.createDirectories(folder);
			this.fileLock = FileChannel.open(folder.resolve(fileToLock), StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE).tryLock();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean thereAreOtherIntances() {
		return this.fileLock == null;
	}
}
