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

package visual.yafs.application;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ApplicationContext {
	public static enum Key {
		//@formatter:off
		TASK_EXECUTOR,
		YAFS_EXECUTOR,
		APPLICATION_SINGLE_INSTANCE_MANAGER
		//@formatter:on
	};

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Key attributeKey) {
		return (T) this.attributes.get(attributeKey);
	}

	public void setAttribute(Key attributeKey, Object attributeValue) {
		this.attributes.put(attributeKey, attributeValue);
	}

	private ConcurrentMap<Key, Object> attributes = new ConcurrentHashMap<>();
}
