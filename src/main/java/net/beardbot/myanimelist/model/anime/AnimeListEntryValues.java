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

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Data;
import net.beardbot.myanimelist.model.adapter.BooleanAdapter;
import net.beardbot.myanimelist.model.adapter.CommaSeperatedListAdapter;
import net.beardbot.myanimelist.model.adapter.OutputDateAdapter;

@Data
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimeListEntryValues {

    @XmlElement(name = "episode")
    private Integer episode;

    @XmlElement(name = "status")
    private AnimeListEntryStatus status;

    @XmlElement(name = "score")
    private Integer score;

    @XmlElement(name = "storage_type")
    private StorageType storageType;

    @XmlElement(name = "storage_value")
    private Float storageValue;

    @XmlElement(name = "times_rewatched")
    private Integer timesRewatched;

    @XmlElement(name = "rewatch_value")
    private RewatchValue rewatchValue;

    @XmlElement(name = "date_start")
    @XmlJavaTypeAdapter(OutputDateAdapter.class)
    private Date dateStart;

    @XmlElement(name = "date_finish")
    @XmlJavaTypeAdapter(OutputDateAdapter.class)
    private Date dateFinish;

    @XmlElement(name = "priority")
    private Integer priority;

    @XmlElement(name = "enable_discussion")
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean enableDiscussion;

    @XmlElement(name = "enable_rewatching")
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean enableRewatching;

    @XmlElement(name = "comments")
    private String comments;

    @XmlElement(name = "fansub_group")
    private String fansubGroup;

    @XmlElement(name = "tags")
    @XmlJavaTypeAdapter(CommaSeperatedListAdapter.class)
    private List<String> tags;

    /**
     * Creates {@link AnimeListEntryValues} based on a {@link AnimeListEntry} object.
     *
     * @param entry {@code [required]} A {@link AnimeListEntry} object containing information about the entry.
     * @return A {@link AnimeListEntryValues} containing all shared information provided with the given {@code entry}.
     * @throws NullPointerException If the entry is not provided.
     */
    public static AnimeListEntryValues fromEntry(AnimeListEntry entry){
        AnimeListEntryValues values = new AnimeListEntryValues();

        values.setEpisode(entry.getWatchedEpisodes());
        values.setScore(entry.getUserScore());
        values.setStatus(entry.getStatus());
        values.setTags(entry.getTags());
        values.setDateStart(entry.getStartedWatching());
        values.setDateFinish(entry.getFinishedWatching());
        values.setEnableRewatching(entry.getRewatching());

        return values;
    }
}