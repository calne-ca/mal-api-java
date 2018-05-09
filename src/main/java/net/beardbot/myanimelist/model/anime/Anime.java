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

import lombok.Data;
import net.beardbot.myanimelist.model.adapter.InputDateAdapter;
import net.beardbot.myanimelist.model.adapter.SemicolonSeperatedListAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@Data
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class Anime {
    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "english")
    private String englishTitle;

    @XmlElement(name = "synonyms")
    @XmlJavaTypeAdapter(SemicolonSeperatedListAdapter.class)
    private List<String> synonyms;

    @XmlElement(name = "episodes")
    private Integer episodes;

    @XmlElement(name = "score")
    private Float score;

    @XmlElement(name = "type")
    private AnimeType type;

    @XmlElement(name = "status")
    private AnimeStatus status;

    @XmlElement(name = "start_date")
    @XmlJavaTypeAdapter(InputDateAdapter.class)
    private Date startDate;

    @XmlElement(name = "end_date")
    @XmlJavaTypeAdapter(InputDateAdapter.class)
    private Date endDate;

    @XmlElement(name = "synopsis")
    private String synopsis;

    @XmlElement(name = "image")
    private String imageUrl;
}
