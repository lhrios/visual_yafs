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

package visual.yafs.xml.fat;

import java.util.Comparator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class FATElement {
	public static Comparator<FATElement> CASE_INSENSITIVE_NAME_COMPARATOR = new Comparator<FATElement>() {
		@Override
		public int compare(FATElement e1, FATElement e2) {
			String name1 = e1.getLongName() == null ? e1.getShortName() : e1.getLongName();
			String name2 = e2.getLongName() == null ? e2.getShortName() : e2.getLongName();
			return name1.compareToIgnoreCase(name2);
		}
	};

	public static Comparator<FATElement> DIRECTORY_FIRST_COMPARATOR = new Comparator<FATElement>() {
		@Override
		public int compare(FATElement e1, FATElement e2) {
			boolean isE1Directory = e1 instanceof FATDirectory;
			boolean isE2Directory = e2 instanceof FATDirectory;

			if (isE1Directory == isE2Directory) {
				return 0;

			} else if (isE1Directory) {
				return -1;

			} else {
				assert isE2Directory;
				return 1;
			}
		}
	};

	private String longName;
	private String shortName;
	private int order;

	@XmlElement(name = "long_name")
	public String getLongName() {
		return this.longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	@XmlElement(name = "short_name")
	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@XmlAttribute
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
