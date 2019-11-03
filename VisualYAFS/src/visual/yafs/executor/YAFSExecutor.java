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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import visual.yafs.common.VisualYAFSConstants;
import visual.yafs.utils.IOUtils;
import visual.yafs.utils.SystemUtils;
import visual.yafs.utils.SystemUtils.OperatingSystem;

public class YAFSExecutor {
	private static final String YAFS_EXECUTABLE_PREFIX = "yafs";
	private static final String YAFS_SUBFOLDER = "yafs";

	private final Path visualYAFStemporaryFolder;
	private final Path yafsFolder;

	public YAFSExecutor(Path visualYAFStemporaryFolder) {
		this.visualYAFStemporaryFolder = visualYAFStemporaryFolder;
		this.yafsFolder = visualYAFStemporaryFolder.resolve(YAFS_SUBFOLDER);
	}

	public void prepareAndCheckYAFS() throws Exception {
		final int BUFFER_SIZE = 1024;

		Files.createDirectories(this.yafsFolder);
		byte buffer[] = new byte[BUFFER_SIZE];

		/* Find the YAFS version that is compatible with the operating system being executed. */
		String yafsZipFileResource;
		if (SystemUtils.HOST_OPERATING_SYSTEM != null) {
			yafsZipFileResource = "/yafs/yafs_" + SystemUtils.HOST_OPERATING_SYSTEM.name().toLowerCase() + ".zip";

		} else {
			throw new Exception(String.format("The operating system \"%s\" is not compatible with Visual YAFS.", SystemUtils.getOperatingSystemName()));
		}

		/* It extracts the .zip file that contains YAFS. */
		try (InputStream inputStream = this.getClass().getResourceAsStream(yafsZipFileResource)) {
			if (inputStream != null) {
				try (ZipInputStream zipInputSream = new ZipInputStream(inputStream)) {
					for (ZipEntry zipEntry = zipInputSream.getNextEntry(); null != zipEntry; zipEntry = zipInputSream.getNextEntry()) {
						Path zipEntryPah = this.yafsFolder.resolve(zipEntry.getName());
						if (!Files.exists(zipEntryPah)) {
							Files.createFile(zipEntryPah);
						}

						File zipEntryFile = zipEntryPah.toFile();
						zipEntryFile.setReadable(true);
						zipEntryFile.setExecutable(true);
						zipEntryFile.setWritable(true);

						try (OutputStream outputStream = new FileOutputStream(zipEntryFile)) {
							while (true) {
								int bytesRead = zipInputSream.read(buffer, 0, BUFFER_SIZE);
								if (bytesRead == -1) {
									break;
								} else {
									outputStream.write(buffer, 0, bytesRead);
								}
							}
						}
					}
				}

			} else {
				throw new Exception(String.format("No YAFS version found to \"%s\" operating system.", SystemUtils.getOperatingSystemName()));
			}
		}

		YAFSExecutionResult executionResult = this.executeYAFS(YAFSParameters.parametersToExecuteHelp());
		if (!executionResult.hasSucceeded()) {
			throw new YAFSExecutionException(executionResult);

		} else {
			String availableYAFSVersion = this.extractVersion(executionResult.getStdout());
			if (!VisualYAFSConstants.EXPECTED_YAFS_VERSION.equals(availableYAFSVersion)) {
				throw new Exception(String.format("Versions does not match. Expected \"%s\" but found \"%s\".", VisualYAFSConstants.EXPECTED_YAFS_VERSION,
						availableYAFSVersion));
			}
		}
	}

	public YAFSExecutionResult executeYAFS(YAFSParameters parameters) {
		YAFSExecutionResult executionResult = new YAFSExecutionResult();
		try {
			File stderrFile = this.visualYAFStemporaryFolder.resolve("stderr").toFile();
			File stdoutFile = this.visualYAFStemporaryFolder.resolve("stdout").toFile();

			List<String> command = parameters.toList();
			if (SystemUtils.HOST_OPERATING_SYSTEM == OperatingSystem.WINDOWS) {
				command.add(0, this.yafsFolder.resolve(YAFS_EXECUTABLE_PREFIX + ".exe").toString());
			} else {
				command.add(0, this.yafsFolder.resolve(YAFS_EXECUTABLE_PREFIX).toString());
			}

			Process process = new ProcessBuilder().directory(this.yafsFolder.toFile()).redirectError(stderrFile).redirectOutput(stdoutFile).command(command).start();
			executionResult.setExitStatus(process.waitFor());
			executionResult.setStderr(IOUtils.contentAsString(stderrFile));
			executionResult.setStdout(IOUtils.contentAsString(stdoutFile));

		} catch (Throwable throwable) {
			executionResult.setThrowable(throwable);
		}

		return executionResult;
	}

	private String extractVersion(String stdout) {
		/* Find the last word at the first line. */
		int lastSpaceIndex = -1;
		for (int i = 0; i < stdout.length(); i++) {
			char c = stdout.charAt(i);
			if (c == ' ') {
				lastSpaceIndex = i;

			} else if (c == '\r' || c == '\n') {
				if (lastSpaceIndex != -1) {
					return stdout.substring(lastSpaceIndex + 1, i);
				}
			}
		}

		return null;
	}
}
