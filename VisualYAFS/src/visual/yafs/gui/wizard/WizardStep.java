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

public enum WizardStep {
	//@formatter:off
	DEVICE_SELECTION("Device selection"),
	SORT_CRITERION_SELECTION("Choice of the sort criterion"),
	CONFIRMATION("Confirmation of the execution plan"),
	YAFS_EXECUTION("YAFS execution");
	//@formatter:on
	private String description;

	private WizardStep(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
