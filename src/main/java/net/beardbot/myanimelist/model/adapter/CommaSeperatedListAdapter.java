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

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CommaSeperatedListAdapter extends XmlAdapter<String,List<String>> {
    @Override
    public List<String> unmarshal(String v) throws Exception {
        List<String> values = new ArrayList<>();

        if (!StringUtils.isBlank(v)){
            StringTokenizer tokenizer = new StringTokenizer(v,",");
            while (tokenizer.hasMoreTokens()){
                values.add(tokenizer.nextToken().trim());
            }
        }

        return values;
    }

    @Override
    public String marshal(List<String> v) throws Exception {
        if(v == null){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.size(); i++) {
            sb.append(v.get(i));

            if (i < v.size()-1){
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}
