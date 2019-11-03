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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public interface IOUtils {
	public static String contentAsString(File file) throws FileNotFoundException, IOException {
		StringBuilder stringBuilder = new StringBuilder();

		try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
			final int BUFFER_SIZE = 1024;
			char[] buffer = new char[BUFFER_SIZE];

			while (true) {
				int charsRead = reader.read(buffer, 0, BUFFER_SIZE);
				if (charsRead != -1) {
					stringBuilder.append(buffer, 0, charsRead);
				} else {
					break;
				}
			}
		}

		return stringBuilder.toString();
	}
}
