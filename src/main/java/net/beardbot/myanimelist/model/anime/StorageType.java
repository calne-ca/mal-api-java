/**
 * Copyright (C) 2018 Joscha Düringer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.beardbot.myanimelist.model.anime;

import javax.xml.bind.annotation.XmlEnumValue;

public enum StorageType {
    @XmlEnumValue("1") HARD_DRIVE("1"),
    @XmlEnumValue("2") DVD_CD("2"),
    @XmlEnumValue("3") NONE("3"),
    @XmlEnumValue("4") RETAIL_DVD("4"),
    @XmlEnumValue("5") VHS("5"),
    @XmlEnumValue("6") EXTERNAL_HD("6"),
    @XmlEnumValue("7") NAS("7");

    private String value;

    StorageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
