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

public enum SortCriterion {

	//@formatter:off
	LEXICOGRAPHICAL_ASCENDING_CASE_INSENSITIVE_FOLDERS_FIRST("Lexicographically ascending (case-insensitive), folders first"),
	LEXICOGRAPHICAL_ASCENDING_CASE_INSENSITIVE_FOLDERS_LAST("Lexicographically ascending (case-insensitive), folders last"),

	LEXICOGRAPHICAL_DESCENDING_CASE_INSENSITIVE_FOLDERS_FIRST("Lexicographically descending (case-insensitive), folders first"),
	LEXICOGRAPHICAL_DESCENDING_CASE_INSENSITIVE_FOLDERS_LAST("Lexicographically descending (case-insensitive), folders last");
	//@formatter:on
	private String description;

	private SortCriterion(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
