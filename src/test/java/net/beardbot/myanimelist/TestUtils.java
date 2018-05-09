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
package net.beardbot.myanimelist;

import lombok.Getter;
import net.beardbot.myanimelist.model.anime.*;
import net.beardbot.myanimelist.model.manga.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestUtils {
    private static final String MALAPPINFO_TEMPLATE = "malappinfo_template.xml";

    private static final String USER_TEMPLATE = "user_template.xml";

    private static final String ANIME_TEMPLATE = "anime_template.xml";
    private static final String ANIME_SEARCH_RESULT_TEMPLATE = "anime_searchresult_template.xml";
    private static final String ANIMELIST_ENTRY_TEMPLATE = "animelist_entry_template.xml";
    private static final String ANIMELIST_MYINFO_TEMPLATE = "animelist_myinfo_template.xml";
    private static final String ANIMELIST_ENTRY_VALUES_TEMPLATE = "animelist_entry_values_template.xml";

    private static final String MANGA_TEMPLATE = "manga_template.xml";
    private static final String MANGA_SEARCH_RESULT_TEMPLATE = "manga_searchresult_template.xml";
    private static final String MANGALIST_ENTRY_TEMPLATE = "mangalist_entry_template.xml";
    private static final String MANGALIST_MYINFO_TEMPLATE = "mangalist_myinfo_template.xml";
    private static final String MANGALIST_ENTRY_VALUES_TEMPLATE = "mangalist_entry_values_template.xml";

    private static Map<String,String> templates = new HashMap<>();

    public static UserXmlBuilder userXmlBuilder(){
        return new UserXmlBuilder();
    }

    public static AnimeSearchResultsXmlBuilder animeSearchResultsXmlBuilder(){
        return new AnimeSearchResultsXmlBuilder();
    }

    public static AnimeXmlBuilder animeXmlBuilder(){
        return new AnimeXmlBuilder();
    }

    public static AnimeListMyInfoXmlBuilder animeListMyInfoXmlBuilder(){
        return new AnimeListMyInfoXmlBuilder();
    }

    public static AnimeListEntryXmlBuilder animeListEntryXmlBuilder(){
        return new AnimeListEntryXmlBuilder();
    }

    public static AnimeListXmlBuilder animeListXmlBuilder(){
        return new AnimeListXmlBuilder();
    }


    public static MangaSearchResultsXmlBuilder mangaSearchResultsXmlBuilder(){
        return new MangaSearchResultsXmlBuilder();
    }

    public static MangaXmlBuilder mangaXmlBuilder(){
        return new MangaXmlBuilder();
    }

    public static MangaListMyInfoXmlBuilder mangaListMyInfoXmlBuilder(){
        return new MangaListMyInfoXmlBuilder();
    }

    public static MangaListEntryXmlBuilder mangaListEntryXmlBuilder(){
        return new MangaListEntryXmlBuilder();
    }

    public static MangaListXmlBuilder mangaListXmlBuilder(){
        return new MangaListXmlBuilder();
    }

    @Getter
    public static class UserXmlBuilder {
        private String id;
        private String username;

        private UserXmlBuilder(){}

        public UserXmlBuilder withId(String id){
            this.id = id;
            return this;
        }
        public UserXmlBuilder withUsername(String username){
            this.username = username;
            return this;
        }
        public String build() throws IOException {
            String template = getTemplate(USER_TEMPLATE);
            return template
                    .replace("{id}",id)
                    .replace("{username}",username);
        }
    }

    @Getter
    public static class AnimeListXmlBuilder {
        private String myInfo;
        private List<String> entries = new ArrayList<>();

        private AnimeListXmlBuilder(){}

        public AnimeListXmlBuilder withMyInfo(String myInfo){
            this.myInfo = myInfo;
            return this;
        }
        public AnimeListXmlBuilder withEntry(String entry){
            this.entries.add(entry);
            return this;
        }
        public String build() throws IOException {
            String template = getTemplate(MALAPPINFO_TEMPLATE);
            StringBuilder sb = new StringBuilder();

            entries.forEach(e->sb.append(e).append(System.lineSeparator()));

            return template
                    .replace("{myinfo}",myInfo)
                    .replace("{entries}",sb.toString());
        }
    }

    @Getter
    public static class AnimeListEntryXmlBuilder {
        private String seriesId;
        private String seriesTitle;
        private String seriesSynonyms;
        private String seriesType;
        private String seriesEpisodes;
        private String seriesStatus;
        private String seriesStart;
        private String seriesEnd;
        private String seriesImage;
        private String myId;
        private String myWatchedEpisodes;
        private String myStartDate;
        private String myFinishDate;
        private String myScore;
        private String myStatus;
        private String myRewatching;
        private String myRewatchingEp;
        private String myLastUpdated;
        private String myTags;

        private AnimeListEntryXmlBuilder(){}

        public AnimeListEntryXmlBuilder withSeriesId(String seriesId){
            this.seriesId = seriesId;
            return this;
        }
        public AnimeListEntryXmlBuilder withSeriesTitle(String seriesTitle){
            this.seriesTitle = seriesTitle;
            return this;
        }
        public AnimeListEntryXmlBuilder withSeriesSynonyms(String seriesSynonyms){
            this.seriesSynonyms = seriesSynonyms;
            return this;
        }
        public AnimeListEntryXmlBuilder withSeriesType(String seriesType){
            this.seriesType = seriesType;
            return this;
        }
        public AnimeListEntryXmlBuilder withSeriesEpisodes(String seriesEpisodes){
            this.seriesEpisodes = seriesEpisodes;
            return this;
        }
        public AnimeListEntryXmlBuilder withSeriesStatus(String seriesStatus){
            this.seriesStatus = seriesStatus;
            return this;
        }
        public AnimeListEntryXmlBuilder withSeriesStart(String seriesStart){
            this.seriesStart = seriesStart;
            return this;
        }
        public AnimeListEntryXmlBuilder withSeriesEnd(String seriesEnd){
            this.seriesEnd = seriesEnd;
            return this;
        }
        public AnimeListEntryXmlBuilder withSeriesImage(String seriesImage){
            this.seriesImage = seriesImage;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyId(String myId){
            this.myId = myId;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyWatchedEpisodes(String myWatchedEpisodes){
            this.myWatchedEpisodes = myWatchedEpisodes;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyStartDate(String myStartDate){
            this.myStartDate = myStartDate;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyFinishDate(String myFinishDate){
            this.myFinishDate = myFinishDate;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyScore(String myScore){
            this.myScore = myScore;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyStatus(String myStatus){
            this.myStatus = myStatus;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyRewatching(String myRewatching){
            this.myRewatching = myRewatching;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyRewatchingEp(String myRewatchingEp){
            this.myRewatchingEp = myRewatchingEp;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyLastUpdated(String myLastUpdated){
            this.myLastUpdated = myLastUpdated;
            return this;
        }
        public AnimeListEntryXmlBuilder withMyTags(String myTags){
            this.myTags = myTags;
            return this;
        }

        public AnimeListEntryXmlBuilder withDefaultValues(){
            return this.withSeriesId(RandomStringUtils.randomNumeric(3))
                    .withSeriesTitle(RandomStringUtils.randomAlphabetic(16))
                    .withSeriesSynonyms(String.format("%s;%s",RandomStringUtils.randomAlphabetic(16),RandomStringUtils.randomAlphabetic(16)))
                    .withSeriesType("1")
                    .withSeriesEpisodes(RandomStringUtils.randomNumeric(2))
                    .withSeriesStatus("2")
                    .withSeriesStart("2004-04-07")
                    .withSeriesEnd("2005-09-28")
                    .withSeriesImage(String.format("https://myanimelist.cdn-dena.com/images/anime/%s/%s.jpg",RandomStringUtils.randomNumeric(1),RandomStringUtils.randomNumeric(5)))
                    .withMyId(RandomStringUtils.randomNumeric(3))
                    .withMyWatchedEpisodes(RandomStringUtils.randomNumeric(3))
                    .withMyStartDate("2013-07-26")
                    .withMyFinishDate("2013-07-28")
                    .withMyScore(RandomStringUtils.randomNumeric(1))
                    .withMyStatus("2")
                    .withMyRewatching("0")
                    .withMyRewatchingEp(RandomStringUtils.randomNumeric(1))
                    .withMyLastUpdated("1375033955")
                    .withMyTags(String.format("%s, %s",RandomStringUtils.randomAlphabetic(16),RandomStringUtils.randomAlphabetic(16)));
        }

        public String build() throws IOException {
            String template = getTemplate(ANIMELIST_ENTRY_TEMPLATE);
            return template
                    .replace("{series_animedb_id}",seriesId)
                    .replace("{series_title}",seriesTitle)
                    .replace("{series_synonyms}",seriesSynonyms)
                    .replace("{series_type}",seriesType)
                    .replace("{series_episodes}",seriesEpisodes)
                    .replace("{series_status}",seriesStatus)
                    .replace("{series_start}",seriesStart)
                    .replace("{series_end}",seriesEnd)
                    .replace("{series_image}",seriesImage)
                    .replace("{my_id}",myId)
                    .replace("{my_watched_episodes}",myWatchedEpisodes)
                    .replace("{my_start_date}",myStartDate)
                    .replace("{my_finish_date}",myFinishDate)
                    .replace("{my_score}",myScore)
                    .replace("{my_status}",myStatus)
                    .replace("{my_rewatching}",myRewatching)
                    .replace("{my_rewatching_ep}",myRewatchingEp)
                    .replace("{my_last_updated}",myLastUpdated)
                    .replace("{my_tags}",myTags);
        }
    }

    @Getter
    public static class AnimeListMyInfoXmlBuilder {
        private String userId;
        private String userName;
        private String watching;
        private String completed;
        private String onHold;
        private String dropped;
        private String planToWatch;
        private String daysSpentWatching;

        private AnimeListMyInfoXmlBuilder(){}

        public AnimeListMyInfoXmlBuilder withUserId(String userId){
            this.userId = userId;
            return this;
        }
        public AnimeListMyInfoXmlBuilder withUserName(String userName){
            this.userName = userName;
            return this;
        }
        public AnimeListMyInfoXmlBuilder withWatching(String watching){
            this.watching = watching;
            return this;
        }
        public AnimeListMyInfoXmlBuilder withCompleted(String completed){
            this.completed = completed;
            return this;
        }
        public AnimeListMyInfoXmlBuilder withOnHold(String onHold){
            this.onHold = onHold;
            return this;
        }
        public AnimeListMyInfoXmlBuilder withDropped(String dropped){
            this.dropped = dropped;
            return this;
        }
        public AnimeListMyInfoXmlBuilder withPlanToWatch(String planToWatch){
            this.planToWatch = planToWatch;
            return this;
        }
        public AnimeListMyInfoXmlBuilder withDaysSpentWatching(String daysSpentWatching){
            this.daysSpentWatching = daysSpentWatching;
            return this;
        }
        public AnimeListMyInfoXmlBuilder withDefaultValues(){
            return this
                    .withUserId(RandomStringUtils.randomNumeric(7))
                    .withUserName(RandomStringUtils.randomAlphabetic(8))
                    .withWatching(RandomStringUtils.randomNumeric(2))
                    .withCompleted(RandomStringUtils.randomNumeric(2))
                    .withOnHold(RandomStringUtils.randomNumeric(2))
                    .withDropped(RandomStringUtils.randomNumeric(2))
                    .withPlanToWatch(RandomStringUtils.randomNumeric(2))
                    .withDaysSpentWatching(String.format("%s.%s",RandomStringUtils.randomNumeric(3),RandomStringUtils.randomNumeric(2)));
        }
        public String build() throws IOException {
            String template = getTemplate(ANIMELIST_MYINFO_TEMPLATE);
            return template
                    .replace("{user_id}",userId)
                    .replace("{user_name}",userName)
                    .replace("{user_watching}",watching)
                    .replace("{user_completed}",completed)
                    .replace("{user_onhold}",onHold)
                    .replace("{user_dropped}",dropped)
                    .replace("{user_plantowatch}",planToWatch)
                    .replace("{user_days_spent_watching}",daysSpentWatching);
        }
    }

    @Getter
    public static class AnimeSearchResultsXmlBuilder{
        private List<String> entries = new ArrayList<>();

        private AnimeSearchResultsXmlBuilder(){}

        public AnimeSearchResultsXmlBuilder withEntry(String entry){
            entries.add(entry);
            return this;
        }
        public String build() throws IOException {
            String template = getTemplate(ANIME_SEARCH_RESULT_TEMPLATE);
            StringBuilder sb = new StringBuilder();
            entries.forEach(e-> sb.append(e).append(System.lineSeparator()));
            return template.replace("{entries}",sb.toString());
        }
    }

    @Getter
    public  static class AnimeXmlBuilder{
        private String id;
        private String title;
        private String english;
        private String synonyms;
        private String episodes;
        private String score;
        private String type;
        private String status;
        private String startDate;
        private String endDate;
        private String synopsis;
        private String image;

        private AnimeXmlBuilder(){}

        public AnimeXmlBuilder withId(String id){
            this.id = id;
            return this;
        }
        public AnimeXmlBuilder withTitle(String title){
            this.title = title;
            return this;
        }
        public AnimeXmlBuilder withEnglish(String english){
            this.english = english;
            return this;
        }
        public AnimeXmlBuilder withSynonyms(String synonyms){
            this.synonyms = synonyms;
            return this;
        }
        public AnimeXmlBuilder withEpisodes(String episodes){
            this.episodes = episodes;
            return this;
        }
        public AnimeXmlBuilder withScore(String score){
            this.score = score;
            return this;
        }
        public AnimeXmlBuilder withType(String type){
            this.type = type;
            return this;
        }
        public AnimeXmlBuilder withStatus(String status){
            this.status = status;
            return this;
        }
        public AnimeXmlBuilder withStartDate(String startDate){
            this.startDate = startDate;
            return this;
        }
        public AnimeXmlBuilder withEndDate(String endDate){
            this.endDate = endDate;
            return this;
        }
        public AnimeXmlBuilder withSynopsis(String synopsis){
            this.synopsis = synopsis;
            return this;
        }
        public AnimeXmlBuilder withImage(String image){
            this.image = image;
            return this;
        }
        public AnimeXmlBuilder withDefaultValues(){
            withId(RandomStringUtils.randomNumeric(3));
            withTitle(RandomStringUtils.randomAlphabetic(16));
            withEnglish(RandomStringUtils.randomAlphabetic(16));
            withSynonyms(String.format("%s;%s",RandomStringUtils.randomAlphabetic(16),RandomStringUtils.randomAlphabetic(16)));
            withEpisodes(RandomStringUtils.randomNumeric(2));
            withScore(String.format("%s.%s",RandomStringUtils.randomNumeric(1),RandomStringUtils.randomNumeric(2)));
            withType("TV");
            withStatus("Finished Airing");
            withStartDate("2006-01-07");
            withEndDate("2006-06-17");
            withSynopsis(RandomStringUtils.randomAlphabetic(1024));
            withImage(String.format("https://myanimelist.cdn-dena.com/images/anime/%s/%s.jpg",RandomStringUtils.randomNumeric(1),RandomStringUtils.randomNumeric(5)));
            return this;
        }
        public String build() throws IOException {
            String template = getTemplate(ANIME_TEMPLATE);
            return template
                    .replace("{id}",id)
                    .replace("{title}",title)
                    .replace("{english}",english)
                    .replace("{synonyms}",synonyms)
                    .replace("{episodes}",episodes)
                    .replace("{score}",score)
                    .replace("{type}",type)
                    .replace("{status}",status)
                    .replace("{start_date}",startDate)
                    .replace("{end_date}",endDate)
                    .replace("{synopsis}",synopsis)
                    .replace("{image}",image);
        }
    }

    @Getter
    public static class MangaListXmlBuilder {
        private String myInfo;
        private List<String> entries = new ArrayList<>();

        private MangaListXmlBuilder(){}

        public MangaListXmlBuilder withMyInfo(String myInfo){
            this.myInfo = myInfo;
            return this;
        }
        public MangaListXmlBuilder withEntry(String entry){
            this.entries.add(entry);
            return this;
        }
        public String build() throws IOException {
            String template = getTemplate(MALAPPINFO_TEMPLATE);
            StringBuilder sb = new StringBuilder();

            entries.forEach(e->sb.append(e).append(System.lineSeparator()));

            return template
                    .replace("{myinfo}",myInfo)
                    .replace("{entries}",sb.toString());
        }
    }

    @Getter
    public static class MangaListEntryXmlBuilder {
        private String seriesId;
        private String seriesTitle;
        private String seriesSynonyms;
        private String seriesType;
        private String seriesChapters;
        private String seriesVolumes;
        private String seriesStatus;
        private String seriesStart;
        private String seriesEnd;
        private String seriesImage;
        private String myId;
        private String myReadChapters;
        private String myReadVolumes;
        private String myStartDate;
        private String myFinishDate;
        private String myScore;
        private String myStatus;
        private String myRereading;
        private String myRereadingChap;
        private String myLastUpdated;
        private String myTags;

        private MangaListEntryXmlBuilder(){}

        public MangaListEntryXmlBuilder withSeriesId(String seriesId){
            this.seriesId = seriesId;
            return this;
        }
        public MangaListEntryXmlBuilder withSeriesTitle(String seriesTitle){
            this.seriesTitle = seriesTitle;
            return this;
        }
        public MangaListEntryXmlBuilder withSeriesSynonyms(String seriesSynonyms){
            this.seriesSynonyms = seriesSynonyms;
            return this;
        }
        public MangaListEntryXmlBuilder withSeriesType(String seriesType){
            this.seriesType = seriesType;
            return this;
        }
        public MangaListEntryXmlBuilder withSeriesChapters(String seriesChapters){
            this.seriesChapters = seriesChapters;
            return this;
        }
        public MangaListEntryXmlBuilder withSeriesVolumes(String seriesVolumes){
            this.seriesVolumes = seriesVolumes;
            return this;
        }
        public MangaListEntryXmlBuilder withSeriesStatus(String seriesStatus){
            this.seriesStatus = seriesStatus;
            return this;
        }
        public MangaListEntryXmlBuilder withSeriesStart(String seriesStart){
            this.seriesStart = seriesStart;
            return this;
        }
        public MangaListEntryXmlBuilder withSeriesEnd(String seriesEnd){
            this.seriesEnd = seriesEnd;
            return this;
        }
        public MangaListEntryXmlBuilder withSeriesImage(String seriesImage){
            this.seriesImage = seriesImage;
            return this;
        }
        public MangaListEntryXmlBuilder withMyId(String myId){
            this.myId = myId;
            return this;
        }
        public MangaListEntryXmlBuilder withMyReadChapters(String myReadChapters){
            this.myReadChapters = myReadChapters;
            return this;
        }
        public MangaListEntryXmlBuilder withMyReadVolumes(String myReadVolumes){
            this.myReadVolumes = myReadVolumes;
            return this;
        }
        public MangaListEntryXmlBuilder withMyStartDate(String myStartDate){
            this.myStartDate = myStartDate;
            return this;
        }
        public MangaListEntryXmlBuilder withMyFinishDate(String myFinishDate){
            this.myFinishDate = myFinishDate;
            return this;
        }
        public MangaListEntryXmlBuilder withMyScore(String myScore){
            this.myScore = myScore;
            return this;
        }
        public MangaListEntryXmlBuilder withMyStatus(String myStatus){
            this.myStatus = myStatus;
            return this;
        }
        public MangaListEntryXmlBuilder withMyRereading(String myRereading){
            this.myRereading = myRereading;
            return this;
        }
        public MangaListEntryXmlBuilder withMyRereadingChap(String myRereadingChap){
            this.myRereadingChap = myRereadingChap;
            return this;
        }
        public MangaListEntryXmlBuilder withMyLastUpdated(String myLastUpdated){
            this.myLastUpdated = myLastUpdated;
            return this;
        }
        public MangaListEntryXmlBuilder withMyTags(String myTags){
            this.myTags = myTags;
            return this;
        }

        public MangaListEntryXmlBuilder withDefaultValues(){
            return this.withSeriesId(RandomStringUtils.randomNumeric(3))
                    .withSeriesTitle(RandomStringUtils.randomAlphabetic(16))
                    .withSeriesSynonyms(String.format("%s;%s",RandomStringUtils.randomAlphabetic(16),RandomStringUtils.randomAlphabetic(16)))
                    .withSeriesType("1")
                    .withSeriesChapters(RandomStringUtils.randomNumeric(3))
                    .withSeriesVolumes(RandomStringUtils.randomNumeric(2))
                    .withSeriesStatus("2")
                    .withSeriesStart("2004-04-07")
                    .withSeriesEnd("2005-09-28")
                    .withSeriesImage(String.format("https://myanimelist.cdn-dena.com/images/anime/%s/%s.jpg",RandomStringUtils.randomNumeric(1),RandomStringUtils.randomNumeric(5)))
                    .withMyId(RandomStringUtils.randomNumeric(3))
                    .withMyReadChapters(RandomStringUtils.randomNumeric(3))
                    .withMyReadVolumes(RandomStringUtils.randomNumeric(2))
                    .withMyStartDate("2013-07-26")
                    .withMyFinishDate("2013-07-28")
                    .withMyScore(RandomStringUtils.randomNumeric(1))
                    .withMyStatus("2")
                    .withMyRereading("0")
                    .withMyRereadingChap(RandomStringUtils.randomNumeric(1))
                    .withMyLastUpdated("1375033955")
                    .withMyTags(String.format("%s, %s",RandomStringUtils.randomAlphabetic(16),RandomStringUtils.randomAlphabetic(16)));
        }

        public String build() throws IOException {
            String template = getTemplate(MANGALIST_ENTRY_TEMPLATE);
            return template
                    .replace("{series_mangadb_id}",seriesId)
                    .replace("{series_title}",seriesTitle)
                    .replace("{series_synonyms}",seriesSynonyms)
                    .replace("{series_type}",seriesType)
                    .replace("{series_chapters}", seriesChapters)
                    .replace("{series_volumes}", seriesVolumes)
                    .replace("{series_status}",seriesStatus)
                    .replace("{series_start}",seriesStart)
                    .replace("{series_end}",seriesEnd)
                    .replace("{series_image}",seriesImage)
                    .replace("{my_id}",myId)
                    .replace("{my_read_chapters}", myReadChapters)
                    .replace("{my_read_volumes}", myReadVolumes)
                    .replace("{my_start_date}",myStartDate)
                    .replace("{my_finish_date}",myFinishDate)
                    .replace("{my_score}",myScore)
                    .replace("{my_status}",myStatus)
                    .replace("{my_rereadingg}", myRereading)
                    .replace("{my_rereading_chap}", myRereadingChap)
                    .replace("{my_last_updated}",myLastUpdated)
                    .replace("{my_tags}",myTags);
        }
    }

    @Getter
    public static class MangaListMyInfoXmlBuilder {
        private String userId;
        private String userName;
        private String reading;
        private String completed;
        private String onHold;
        private String dropped;
        private String planToRead;
        private String daysSpentReading;

        private MangaListMyInfoXmlBuilder(){}

        public MangaListMyInfoXmlBuilder withUserId(String userId){
            this.userId = userId;
            return this;
        }
        public MangaListMyInfoXmlBuilder withUserName(String userName){
            this.userName = userName;
            return this;
        }
        public MangaListMyInfoXmlBuilder withReading(String reading){
            this.reading = reading;
            return this;
        }
        public MangaListMyInfoXmlBuilder withCompleted(String completed){
            this.completed = completed;
            return this;
        }
        public MangaListMyInfoXmlBuilder withOnHold(String onHold){
            this.onHold = onHold;
            return this;
        }
        public MangaListMyInfoXmlBuilder withDropped(String dropped){
            this.dropped = dropped;
            return this;
        }
        public MangaListMyInfoXmlBuilder withPlanToRead(String planToRead){
            this.planToRead = planToRead;
            return this;
        }
        public MangaListMyInfoXmlBuilder withDaysSpentReading(String daysSpentReading){
            this.daysSpentReading = daysSpentReading;
            return this;
        }
        public MangaListMyInfoXmlBuilder withDefaultValues(){
            return this
                    .withUserId(RandomStringUtils.randomNumeric(7))
                    .withUserName(RandomStringUtils.randomAlphabetic(8))
                    .withReading(RandomStringUtils.randomNumeric(2))
                    .withCompleted(RandomStringUtils.randomNumeric(2))
                    .withOnHold(RandomStringUtils.randomNumeric(2))
                    .withDropped(RandomStringUtils.randomNumeric(2))
                    .withPlanToRead(RandomStringUtils.randomNumeric(2))
                    .withDaysSpentReading(String.format("%s.%s",RandomStringUtils.randomNumeric(3),RandomStringUtils.randomNumeric(2)));
        }
        public String build() throws IOException {
            String template = getTemplate(MANGALIST_MYINFO_TEMPLATE);
            return template
                    .replace("{user_id}",userId)
                    .replace("{user_name}",userName)
                    .replace("{user_reading}", reading)
                    .replace("{user_completed}",completed)
                    .replace("{user_onhold}",onHold)
                    .replace("{user_dropped}",dropped)
                    .replace("{user_plantoread}", planToRead)
                    .replace("{user_days_spent_watching}", daysSpentReading);
        }
    }

    @Getter
    public static class MangaSearchResultsXmlBuilder {
        private List<String> entries = new ArrayList<>();

        private MangaSearchResultsXmlBuilder(){}

        public MangaSearchResultsXmlBuilder withEntry(String entry){
            entries.add(entry);
            return this;
        }
        public String build() throws IOException {
            String template = getTemplate(MANGA_SEARCH_RESULT_TEMPLATE);
            StringBuilder sb = new StringBuilder();
            entries.forEach(e-> sb.append(e).append(System.lineSeparator()));
            return template.replace("{entries}",sb.toString());
        }
    }

    @Getter
    public static class MangaXmlBuilder {
        private String id;
        private String title;
        private String english;
        private String synonyms;
        private String chapters;
        private String volumes;
        private String score;
        private String type;
        private String status;
        private String startDate;
        private String endDate;
        private String synopsis;
        private String image;

        private MangaXmlBuilder(){}

        public MangaXmlBuilder withId(String id){
            this.id = id;
            return this;
        }
        public MangaXmlBuilder withTitle(String title){
            this.title = title;
            return this;
        }
        public MangaXmlBuilder withEnglish(String english){
            this.english = english;
            return this;
        }
        public MangaXmlBuilder withSynonyms(String synonyms){
            this.synonyms = synonyms;
            return this;
        }
        public MangaXmlBuilder withChapters(String chapters){
            this.chapters = chapters;
            return this;
        }
        public MangaXmlBuilder withVolumes(String volumes){
            this.volumes = volumes;
            return this;
        }
        public MangaXmlBuilder withScore(String score){
            this.score = score;
            return this;
        }
        public MangaXmlBuilder withType(String type){
            this.type = type;
            return this;
        }
        public MangaXmlBuilder withStatus(String status){
            this.status = status;
            return this;
        }
        public MangaXmlBuilder withStartDate(String startDate){
            this.startDate = startDate;
            return this;
        }
        public MangaXmlBuilder withEndDate(String endDate){
            this.endDate = endDate;
            return this;
        }
        public MangaXmlBuilder withSynopsis(String synopsis){
            this.synopsis = synopsis;
            return this;
        }
        public MangaXmlBuilder withImage(String image){
            this.image = image;
            return this;
        }
        public MangaXmlBuilder withDefaultValues(){
            withId(RandomStringUtils.randomNumeric(3));
            withTitle(RandomStringUtils.randomAlphabetic(16));
            withEnglish(RandomStringUtils.randomAlphabetic(16));
            withSynonyms(String.format("%s;%s",RandomStringUtils.randomAlphabetic(16),RandomStringUtils.randomAlphabetic(16)));
            withChapters(RandomStringUtils.randomNumeric(3));
            withVolumes(RandomStringUtils.randomNumeric(2));
            withScore(String.format("%s.%s",RandomStringUtils.randomNumeric(1),RandomStringUtils.randomNumeric(2)));
            withType("Manga");
            withStatus("Finished");
            withStartDate("2006-01-07");
            withEndDate("2006-06-17");
            withSynopsis(RandomStringUtils.randomAlphabetic(1024));
            withImage(String.format("https://myanimelist.cdn-dena.com/images/manga/%s/%s.jpg",RandomStringUtils.randomNumeric(1),RandomStringUtils.randomNumeric(5)));
            return this;
        }
        public String build() throws IOException {
            String template = getTemplate(MANGA_TEMPLATE);
            return template
                    .replace("{id}",id)
                    .replace("{title}",title)
                    .replace("{english}",english)
                    .replace("{synonyms}",synonyms)
                    .replace("{chapters}",chapters)
                    .replace("{volumes}",volumes)
                    .replace("{score}",score)
                    .replace("{type}",type)
                    .replace("{status}",status)
                    .replace("{start_date}",startDate)
                    .replace("{end_date}",endDate)
                    .replace("{synopsis}",synopsis)
                    .replace("{image}",image);
        }
    }
    
    private static String getTemplate(String template) throws IOException {
        if (templates.get(template) == null){
            InputStream stream = TestUtils.class.getResourceAsStream(template);
            templates.put(template,IOUtils.toString(stream, StandardCharsets.UTF_8));
        }
        return templates.get(template);
    }

    public static Date dateFromString(String dateString){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateString);
        } catch (ParseException ignored) {}
        return null;
    }

    public static Date dateFromEpochSeconds(long seconds){
        return new Date(seconds);
    }

    public static AnimeListEntryValues createTestAnimeListEntryValues(){
        AnimeListEntryValues values = new AnimeListEntryValues();
        values.setStorageType(StorageType.DVD_CD);
        values.setTimesRewatched(12);
        values.setStorageValue(5f);
        values.setTags(Arrays.asList("AAA","BBB"));
        values.setScore(10);
        values.setRewatchValue(RewatchValue.HIGH);
        values.setStatus(AnimeListEntryStatus.ON_HOLD);
        values.setPriority(1);
        values.setFansubGroup("Test Subgroup");
        values.setEpisode(14);
        values.setEnableRewatching(true);
        values.setDateStart(dateFromString("2018-01-02"));
        values.setDateFinish(dateFromString("2018-02-14"));
        values.setEnableDiscussion(true);
        values.setComments("Test comments...");
        return values;
    }

    public static AnimeListEntry createTestAnimeListEntry(){
        AnimeListEntry entry = new AnimeListEntry();
        entry.setEntryId(RandomStringUtils.randomAlphanumeric(16));
        entry.setUserScore(7);
        entry.setWatchedEpisodes(8);
        entry.setRewatching(true);
        entry.setRewatchingEpisodes(0);
        entry.setTags(Arrays.asList(RandomStringUtils.randomAlphanumeric(16),RandomStringUtils.randomAlphanumeric(16)));
        entry.setStartedWatching(dateFromString("2018-01-10"));
        entry.setSeriesId(RandomStringUtils.randomAlphanumeric(16));
        entry.setSeriesEpisodes(12);
        entry.setSeriesStatus(AnimeListSeriesStatus.CURRENTLY_AIRING);
        entry.setSeriesSynonyms(Arrays.asList(RandomStringUtils.randomAlphanumeric(16),RandomStringUtils.randomAlphanumeric(16)));
        entry.setSeriesImageUrl(String.format("https://myanimelist.cdn-dena.com/images/anime/%s/%s.jpg",RandomStringUtils.randomNumeric(1),RandomStringUtils.randomNumeric(5)));
        entry.setSeriesType(AnimeListSeriesType.TV);
        entry.setSeriesStart(dateFromString("2018-01-01"));
        entry.setSeriesEnd(dateFromString("2018-07-04"));
        
        return entry;
    }

    public static MangaListEntryValues createTestMangaListEntryValues(){
        MangaListEntryValues values = new MangaListEntryValues();
        values.setTimesReread(12);
        values.setTags(Arrays.asList("AAA","BBB"));
        values.setScore(10);
        values.setRereadValue(RereadValue.HIGH);
        values.setStatus(MangaListEntryStatus.ON_HOLD);
        values.setPriority(1);
        values.setScanGroup("Test Scangroup");
        values.setChapter(14);
        values.setVolume(2);
        values.setEnableRereading(true);
        values.setDateStart(dateFromString("2018-01-02"));
        values.setDateFinish(dateFromString("2018-02-14"));
        values.setEnableDiscussion(true);
        values.setComments("Test comments...");
        values.setRetailVolumes(5);
        return values;
    }

    public static MangaListEntry createTestMangaListEntry(){
        MangaListEntry entry = new MangaListEntry();
        entry.setEntryId(RandomStringUtils.randomAlphanumeric(16));
        entry.setUserScore(7);
        entry.setReadChapters(12);
        entry.setReadVolumes(2);
        entry.setRereading(true);
        entry.setRereadingChapters(0);
        entry.setTags(Arrays.asList(RandomStringUtils.randomAlphanumeric(16),RandomStringUtils.randomAlphanumeric(16)));
        entry.setStartedReading(dateFromString("2017-01-10"));
        entry.setSeriesId(RandomStringUtils.randomAlphanumeric(16));
        entry.setSeriesChapters(120);
        entry.setSeriesVolumes(8);
        entry.setSeriesStatus(MangaListSeriesStatus.PUBLISHING);
        entry.setSeriesSynonyms(Arrays.asList(RandomStringUtils.randomAlphanumeric(16),RandomStringUtils.randomAlphanumeric(16)));
        entry.setSeriesImageUrl(String.format("https://myanimelist.cdn-dena.com/images/anime/%s/%s.jpg",RandomStringUtils.randomNumeric(1),RandomStringUtils.randomNumeric(5)));
        entry.setSeriesType(MangaListSeriesType.MANGA);
        entry.setSeriesStart(dateFromString("2017-01-01"));
        entry.setSeriesEnd(dateFromString("2017-07-04"));

        return entry;
    }

    public static String createTestAnimeListEntryValuesXml() throws IOException {
        AnimeListEntryValues values = createTestAnimeListEntryValues();
        String template = getTemplate(ANIMELIST_ENTRY_VALUES_TEMPLATE);
        return template
                .replace("{episode}",values.getEpisode().toString())
                .replace("{status}",values.getStatus().getValue())
                .replace("{score}",values.getScore().toString())
                .replace("{storage_type}",values.getStorageType().getValue())
                .replace("{storage_value}",values.getStorageValue().toString())
                .replace("{times_rewatched}",values.getTimesRewatched().toString())
                .replace("{rewatch_value}",values.getRewatchValue().getValue())
                .replace("{date_start}","01022018")
                .replace("{date_finish}","02142018")
                .replace("{priority}",values.getPriority().toString())
                .replace("{enable_discussion}","1")
                .replace("{enable_rewatching}","1")
                .replace("{comments}",values.getComments())
                .replace("{fansub_group}",values.getFansubGroup())
                .replace("{tags}","AAA, BBB");
    }

    public static String createTestMangaListEntryValuesXml() throws IOException {
        MangaListEntryValues values = createTestMangaListEntryValues();
        String template = getTemplate(MANGALIST_ENTRY_VALUES_TEMPLATE);
        return template
                .replace("{chapter}",values.getChapter().toString())
                .replace("{volume}",values.getVolume().toString())
                .replace("{status}",values.getStatus().getValue())
                .replace("{score}",values.getScore().toString())
                .replace("{times_reread}",values.getTimesReread().toString())
                .replace("{reread_value}",values.getRereadValue().getValue())
                .replace("{date_start}","01022018")
                .replace("{date_finish}","02142018")
                .replace("{priority}",values.getPriority().toString())
                .replace("{enable_discussion}","1")
                .replace("{enable_rereading}","1")
                .replace("{comments}",values.getComments())
                .replace("{scan_group}",values.getScanGroup())
                .replace("{tags}","AAA, BBB")
                .replace("{retail_volumes}",values.getRetailVolumes().toString());
    }

    public static String unifyXml(String xml){
        return xml.replace(" ","").replace("\n","").replace("\t","").replace("\r","");
    }
}
