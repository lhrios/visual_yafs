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

package visual.yafs.xml;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import visual.yafs.xml.fat.FATDirectory;
import visual.yafs.xml.fat.FATElement;
import visual.yafs.xml.fat.FATFile;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Root {
	private List<FATElement> children;

	@XmlElements({@XmlElement(name = "file", type = FATFile.class), @XmlElement(name = "directory", type = FATDirectory.class)})
	public List<FATElement> getChildren() {
		return this.children;
	}

	public void setChildren(List<FATElement> children) {
		this.children = children;
	}

	public static void sort(Root root, Comparator<? super FATElement> comparator) {
		if (root.getChildren() != null) {
			Collections.sort(root.getChildren(), comparator);

			int order = 0;
			for (FATElement fatElement : root.getChildren()) {
				fatElement.setOrder(order++);

				if (fatElement instanceof FATDirectory) {
					sort((FATDirectory) fatElement, comparator);
				}
			}
		}
	}

	private static void sort(FATDirectory fatDirectory, Comparator<? super FATElement> comparator) {
		if (fatDirectory.getChildren() != null) {
			Collections.sort(fatDirectory.getChildren(), comparator);

			int order = 0;
			for (FATElement fatElement : fatDirectory.getChildren()) {
				fatElement.setOrder(order++);

				if (fatElement instanceof FATDirectory) {
					sort((FATDirectory) fatElement, comparator);
				}
			}
		}
	}
}
