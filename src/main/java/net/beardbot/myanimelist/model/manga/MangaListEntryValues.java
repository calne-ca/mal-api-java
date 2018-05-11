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

import lombok.Data;
import net.beardbot.myanimelist.model.adapter.BooleanAdapter;
import net.beardbot.myanimelist.model.adapter.CommaSeperatedListAdapter;
import net.beardbot.myanimelist.model.adapter.OutputDateAdapter;

import javax.validation.constraints.NotNull;
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
public class MangaListEntryValues {

    @XmlElement(name = "chapter")
    private Integer chapter;

    @XmlElement(name = "volume")
    private Integer volume;

    @XmlElement(name = "status")
    private MangaListEntryStatus status;

    @XmlElement(name = "score")
    private Integer score;

    @XmlElement(name = "times_reread")
    private Integer timesReread;

    @XmlElement(name = "reread_value")
    private RereadValue rereadValue;

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

    @XmlElement(name = "enable_rereading")
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    private Boolean enableRereading;

    @XmlElement(name = "comments")
    private String comments;

    @XmlElement(name = "scan_group")
    private String scanGroup;

    @XmlElement(name = "tags")
    @XmlJavaTypeAdapter(CommaSeperatedListAdapter.class)
    private List<String> tags;

    @XmlElement(name = "retail_volumes")
    private Integer retailVolumes;

    /**
     * Creates {@link MangaListEntryValues} based on a {@link MangaListEntry} object.
     *
     * @param entry {@code [required]} A {@link MangaListEntry} object containing information about the entry.
     * @return A {@link MangaListEntryValues} containing all shared information provided with the given {@code entry}.
     * @throws NullPointerException If the entry is not provided.
     */
    public static MangaListEntryValues fromEntry(@NotNull MangaListEntry entry){
        MangaListEntryValues values = new MangaListEntryValues();

        values.setChapter(entry.getReadChapters());
        values.setVolume(entry.getReadVolumes());
        values.setScore(entry.getUserScore());
        values.setStatus(entry.getStatus());
        values.setTags(entry.getTags());
        values.setDateStart(entry.getStartedReading());
        values.setDateFinish(entry.getFinishedReading());
        values.setEnableRereading(entry.getRereading());

        return values;
    }
}