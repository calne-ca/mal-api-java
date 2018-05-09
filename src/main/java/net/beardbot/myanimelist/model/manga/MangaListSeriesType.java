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
package net.beardbot.myanimelist.model.manga;

import javax.xml.bind.annotation.XmlEnumValue;

public enum MangaListSeriesType {
    @XmlEnumValue("1") MANGA("1"),
    @XmlEnumValue("2") NOVEL("2"),
    @XmlEnumValue("3") MANHWA("3");

    private String value;

    MangaListSeriesType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
