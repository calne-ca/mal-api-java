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
package net.beardbot.myanimelist.model.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BooleanAdapter extends XmlAdapter<Integer,Boolean> {
    @Override
    public Boolean unmarshal(Integer v) throws Exception {
        return v != null && v != 0;
    }

    @Override
    public Integer marshal(Boolean v) throws Exception {
        return v != null && v ? 1 : 0;
    }
}
