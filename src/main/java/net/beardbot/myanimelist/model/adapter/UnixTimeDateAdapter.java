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
import java.util.Date;

public class UnixTimeDateAdapter extends XmlAdapter<Long,Date> {
    @Override
    public Date unmarshal(Long v) throws Exception {
        return new Date(v);
    }

    @Override
    public Long marshal(Date v) throws Exception {
        return v.getTime();
    }
}
