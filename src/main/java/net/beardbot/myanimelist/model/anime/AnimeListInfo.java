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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "myinfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimeListInfo {

    @XmlElement(name = "user_id")
    private String userId;

    @XmlElement(name = "user_name")
    private String userName;

    @XmlElement(name = "user_watching")
    private Integer watching;

    @XmlElement(name = "user_completed")
    private Integer completed;

    @XmlElement(name = "user_onhold")
    private Integer onHold;

    @XmlElement(name = "user_dropped")
    private Integer dropped;

    @XmlElement(name = "user_plantowatch")
    private Integer planToWatch;

    @XmlElement(name = "user_days_spent_watching")
    private Float daysSpendWatching;
}
