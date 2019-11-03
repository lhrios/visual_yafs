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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import visual.yafs.xml.fat.FATDirectory;
import visual.yafs.xml.fat.FATElement;
import visual.yafs.xml.fat.FATFile;

public class XMLUtils {
	private static final JAXBContext JAXB_CONTEXT;

	static {
		List<Class<?>> marshallingClasses = new ArrayList<>();
		marshallingClasses.add(Root.class);
		marshallingClasses.add(FATDirectory.class);
		marshallingClasses.add(FATElement.class);
		marshallingClasses.add(FATFile.class);

		Class<?>[] marshallingClassesArray = new Class<?>[marshallingClasses.size()];
		int i = 0;
		for (Class<?> clazz : marshallingClasses) {
			marshallingClassesArray[i++] = clazz;
		}

		try {
			JAXB_CONTEXT = JAXBContext.newInstance(marshallingClassesArray);

		} catch (JAXBException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public static Root readXMLFile(Path xmlFile) throws JAXBException {
		Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();

		return (Root) unmarshaller.unmarshal(xmlFile.toFile());
	}

	public static void writeXMLFile(Root root, Path xmlFile) throws JAXBException {
		Marshaller marshaller = JAXB_CONTEXT.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		marshaller.marshal(root, xmlFile.toFile());
	}
}
