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

package visual.yafs.gui.wizard;

import java.nio.file.Path;
import java.util.Comparator;

import visual.yafs.application.ApplicationContext.Key;
import visual.yafs.application.VisualYAFSApplication;
import visual.yafs.executor.YAFSExecutionException;
import visual.yafs.executor.YAFSExecutionResult;
import visual.yafs.executor.YAFSExecutor;
import visual.yafs.executor.YAFSParameters;
import visual.yafs.gui.common.VisualYAFSTask;
import visual.yafs.utils.ComparatorUtils;
import visual.yafs.xml.Root;
import visual.yafs.xml.XMLUtils;
import visual.yafs.xml.fat.FATElement;

public class ExecuteYAFSTask extends VisualYAFSTask<Void> {
	private Path device;
	private SortCriterion sortCriterion;

	public ExecuteYAFSTask(Path device, SortCriterion sortCriterion) {
		this.device = device;
		this.sortCriterion = sortCriterion;
	}

	@Override
	protected Void call() throws Exception {
		Path originalSystemDirectoryTreeFile = VisualYAFSApplication.getInstance().getTemporaryFolder().resolve("original_system_directory_tree.xml");

		/* First, it will read the file system. */
		YAFSParameters parameters = YAFSParameters.parametersToReadSystemDirectoryTree(this.device, originalSystemDirectoryTreeFile);
		YAFSExecutor yafsExecutor = VisualYAFSApplication.getInstance().getApplicationContext().getAttribute(Key.YAFS_EXECUTOR);
		YAFSExecutionResult executionResult = yafsExecutor.executeYAFS(parameters);
		if (!executionResult.hasSucceeded()) {
			throw new YAFSExecutionException(executionResult);
		}

		/* Import the output. */
		Root root = XMLUtils.readXMLFile(originalSystemDirectoryTreeFile);

		/* Sort the entries properly. */
		Comparator<FATElement> nameComparator = FATElement.CASE_INSENSITIVE_NAME_COMPARATOR;
		if (this.sortCriterion == SortCriterion.LEXICOGRAPHICAL_DESCENDING_CASE_INSENSITIVE_FOLDERS_FIRST
				|| this.sortCriterion == SortCriterion.LEXICOGRAPHICAL_DESCENDING_CASE_INSENSITIVE_FOLDERS_LAST) {
			nameComparator = ComparatorUtils.reversedComparator(nameComparator);
		}

		Comparator<FATElement> fileDirectoryComparator = FATElement.DIRECTORY_FIRST_COMPARATOR;
		if (this.sortCriterion == SortCriterion.LEXICOGRAPHICAL_ASCENDING_CASE_INSENSITIVE_FOLDERS_LAST
				|| this.sortCriterion == SortCriterion.LEXICOGRAPHICAL_DESCENDING_CASE_INSENSITIVE_FOLDERS_LAST) {
			fileDirectoryComparator = ComparatorUtils.reversedComparator(fileDirectoryComparator);
		}

		Comparator<FATElement> chainedComparator = ComparatorUtils.chainedComparator(fileDirectoryComparator, nameComparator);
		Root.sort(root, chainedComparator);

		/* Export the output. */
		Path newSystemDirectoryTreeFile = VisualYAFSApplication.getInstance().getTemporaryFolder().resolve("new_system_directory_tree.xml");
		XMLUtils.writeXMLFile(root, newSystemDirectoryTreeFile);

		/* Finally, it executes YAFS to change the device's file system. */
		parameters = YAFSParameters.parametersToWriteSystemDirectoryTree(this.device, newSystemDirectoryTreeFile);
		yafsExecutor = VisualYAFSApplication.getInstance().getApplicationContext().getAttribute(Key.YAFS_EXECUTOR);
		executionResult = yafsExecutor.executeYAFS(parameters);
		if (!executionResult.hasSucceeded()) {
			throw new YAFSExecutionException(executionResult);
		}

		return null;
	}
}
