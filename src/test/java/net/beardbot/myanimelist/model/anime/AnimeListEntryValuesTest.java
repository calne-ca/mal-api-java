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


import org.junit.Test;

import static net.beardbot.myanimelist.TestUtils.createTestAnimeListEntry;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AnimeListEntryValuesTest {

    @Test
    public void AnimeListEntry_to_AnimeListEntryValuesConversion() {
        AnimeListEntry entry = createTestAnimeListEntry();
        AnimeListEntryValues values = AnimeListEntryValues.fromEntry(entry);

        assertThat(values.getEpisode(),is(entry.getWatchedEpisodes()));
        assertThat(values.getScore(),is(entry.getUserScore()));
        assertThat(values.getStatus(),is(entry.getStatus()));
        assertThat(values.getTags(),is(entry.getTags()));
        assertThat(values.getDateStart(),is(entry.getStartedWatching()));
        assertThat(values.getDateFinish(),is(entry.getFinishedWatching()));
        assertThat(values.getEnableRewatching(),is(entry.getRewatching()));
    }
}