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

import java.util.Comparator;

public interface ComparatorUtils {
	public static <T> Comparator<T> reversedComparator(final Comparator<T> comparator) {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return comparator.compare(o2, o1);
			}
		};
	}

	@SafeVarargs
	public static <T> Comparator<T> chainedComparator(final Comparator<? super T>... comparators) {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				int result = 0;
				for (Comparator<? super T> comparator : comparators) {
					result = comparator.compare(o1, o2);
					if (result != 0) {
						return result;
					}
				}
				return result;
			}
		};
	}
}
