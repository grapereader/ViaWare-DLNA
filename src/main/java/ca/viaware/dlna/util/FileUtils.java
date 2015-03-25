/*
 * Copyright 2015 Seth Traverse
 *
 * This file is part of ViaWareDLNAServer.
 *
 * ViaWareDLNAServer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ViaWareDLNAServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ViaWareDLNAServer. If not, see <http://www.gnu.org/licenses/>.
 */

package ca.viaware.dlna.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class FileUtils {

    public static String getMime(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String removeExtension(File file) {
        String name = file.getName();
        if (!name.contains(".") || file.isDirectory()) return name;
        String[] parts = name.split("[.]");
        parts = Arrays.copyOf(parts, parts.length - 1);
        String joined = "";
        for (String s : parts) joined += s;
        return joined;
    }

}
