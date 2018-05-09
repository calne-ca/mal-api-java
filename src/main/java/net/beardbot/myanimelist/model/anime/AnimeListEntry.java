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
import net.beardbot.myanimelist.model.adapter.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@Data
@XmlRootElement(name = "anime")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimeListEntry {

    @XmlElement(name = "series_animedb_id")
    private String seriesId;

    @XmlElement(name = "series_title")
    private String seriesTitle;

    @XmlElement(name = "series_synonyms")
    @XmlJavaTypeAdapter(SemicolonSeperatedListAdapter.class)
    private List<String> seriesSynonyms;

    @XmlElement(name = "series_type")
    private AnimeListSeriesType seriesType;

    @XmlElement(name = "series_episodes")
    private Integer seriesEpisodes;

    @XmlElement(name = "series_status")
    private AnimeListSeriesStatus seriesStatus;

    @XmlElement(name = "series_start")
    @XmlJavaTypeAdapter(InputDateAdapter.class)
    private Date seriesStart;

    @XmlElement(name = "series_end")
    @XmlJavaTypeAdapter(InputDateAdapter.class)
    private Date seriesEnd;

    @XmlElement(name = "series_image")
    private String seriesImageUrl;

    @XmlElement(name = "my_id")
    private String entryId;

    @XmlElement(name = "my_watched_episodes")
    private Integer watchedEpisodes;

    @XmlElement(name = "my_start_date")
    private Date startedWatching;

    @XmlElement(name = "my_finish_date")
    @XmlJavaTypeAdapter(InputDateAdapter.class)
    private Date finishedWatching;

    @XmlElement(name = "my_score")
    private Integer userScore;

    @XmlElement(name = "my_status")
    private AnimeListEntryStatus status;

    @XmlElement(name = "my_rewatching")
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean rewatching;

    @XmlElement(name = "my_rewatching_ep")
    private Integer rewatchingEpisodes;

    @XmlElement(name = "my_last_updated")
    @XmlJavaTypeAdapter(UnixTimeDateAdapter.class)
    private Date lastUpdated;

    @XmlElement(name = "my_tags")
    @XmlJavaTypeAdapter(CommaSeperatedListAdapter.class)
    private List<String> tags;


}