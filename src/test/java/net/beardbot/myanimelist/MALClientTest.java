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

import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import net.beardbot.myanimelist.model.User;
import net.beardbot.myanimelist.model.anime.*;
import net.beardbot.myanimelist.model.manga.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static me.alexpanov.net.FreePortFinder.findFreeLocalPort;
import static net.beardbot.myanimelist.MAL.*;
import static net.beardbot.myanimelist.MAL.PATH_MANGA_DELETE;
import static net.beardbot.myanimelist.MAL.PATH_MANGA_UPDATE;
import static net.beardbot.myanimelist.TestUtils.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class MALClientTest {

    private static final int TEST_PORT = findFreeLocalPort();

    @ClassRule
    public static WireMockClassRule malService = new WireMockClassRule(TEST_PORT);
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private MALClient client;

    private String username;
    private String password;
    private String query;

    @Before
    public void setUp() {
        username = RandomStringUtils.randomAlphanumeric(16);
        password = RandomStringUtils.randomAlphanumeric(16);
        query = RandomStringUtils.randomAlphabetic(8);
        client = new MALClient(username, password, "http://localhost:" + TEST_PORT);
    }

    @After
    public void tearDown() throws Exception {
        malService.resetRequests();
        malService.resetMappings();
        malService.resetScenarios();
    }

    @Test()
    public void throwsNullPointerException_whenUsernameIsNull() {
        expectedException.expect(NullPointerException.class);
        new MALClient(null, password);
    }

    @Test()
    public void throwsNullPointerException_whenPasswordIsNull() {
        expectedException.expect(NullPointerException.class);
        new MALClient(username, null);
    }

    @Test()
    public void throwsNullPointerException_whenUrlIsNull() {
        expectedException.expect(NullPointerException.class);
        new MALClient(username, password, null);
    }

    @Test
    public void getAnimeList_callsMalApi() {
        String url = String.format("%s?u=%s&type=anime&status=all",PATH_MALAPPINFO,username);
        malService.stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withStatus(200)));
        client.getAnimeList(username);
        malService.verify(getRequestedFor(urlEqualTo(url)));
    }
    @Test
    public void searchForAnime_callsMalApi() {
        String url = String.format("%s?q=%s",PATH_ANIME_SEARCH,query);
        malService.stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withStatus(200)));
        client.searchForAnime(query);
        malService.verify(getRequestedFor(urlEqualTo(url)));
    }
    @Test
    public void updateAnimeList_callsMalApi() {
        String id = RandomStringUtils.randomNumeric(3);
        malService.stubFor(post(urlPathEqualTo(PATH_ANIME_UPDATE.replace("%id",id))).willReturn(aResponse().withStatus(200)));
        client.updateAnimeList(id, createTestAnimeListEntryValues());
        malService.verify(postRequestedFor(urlEqualTo(PATH_ANIME_UPDATE.replace("%id",id))));
    }
    @Test
    public void deleteFromAnimeList_callsMalApi() {
        String id = RandomStringUtils.randomNumeric(3);
        malService.stubFor(delete(urlPathEqualTo(PATH_ANIME_DELETE.replace("%id",id))).willReturn(aResponse().withStatus(200)));
        client.deleteFromAnimeList(id);
        malService.verify(deleteRequestedFor(urlEqualTo(PATH_ANIME_DELETE.replace("%id",id))));
    }

    @Test
    public void getMangaList_callsMalApi() {
        String url = String.format("%s?u=%s&type=manga&status=all",PATH_MALAPPINFO,username);
        malService.stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withStatus(200)));
        client.getMangaList(username);
        malService.verify(getRequestedFor(urlEqualTo(url)));
    }
    @Test
    public void searchForManga_callsMalApi() {
        String url = String.format("%s?q=%s",PATH_MANGA_SEARCH,query);
        malService.stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withStatus(200)));
        client.searchForManga(query);
        malService.verify(getRequestedFor(urlEqualTo(url)));
    }
    @Test
    public void updateMangaList_callsMalApi() {
        String id = RandomStringUtils.randomNumeric(3);
        malService.stubFor(post(urlPathEqualTo(PATH_MANGA_UPDATE.replace("%id",id))).willReturn(aResponse().withStatus(200)));
        client.updateMangaList(id, createTestMangaListEntryValues());
        malService.verify(postRequestedFor(urlEqualTo(PATH_MANGA_UPDATE.replace("%id",id))));
    }
    @Test
    public void deleteFromMangaList_callsMalApi() {
        String id = RandomStringUtils.randomNumeric(3);
        malService.stubFor(delete(urlPathEqualTo(PATH_MANGA_DELETE.replace("%id",id))).willReturn(aResponse().withStatus(200)));
        client.deleteFromMangaList(id);
        malService.verify(deleteRequestedFor(urlEqualTo(PATH_MANGA_DELETE.replace("%id",id))));
    }
    @Test
    public void verifyCredentials_callsMalApi() {
        malService.stubFor(get(urlPathEqualTo(PATH_VERIFY_CREDENTIALS)).willReturn(aResponse().withStatus(200)));
        client.verifyCredentials();
        malService.verify(getRequestedFor(urlEqualTo(PATH_VERIFY_CREDENTIALS)));
    }

    @Test
    public void animeSearch_noResults() throws Exception {
        malService.stubFor(get(urlPathEqualTo(PATH_ANIME_SEARCH)).willReturn(aResponse()
                .withStatus(204)));

        List<Anime> results = client.searchForAnime(query);
        assertThat(results.size(),is(0));
    }

    @Test
    public void getAnimeList_noResults() throws Exception {
        malService.stubFor(get(urlPathEqualTo(PATH_MALAPPINFO)).willReturn(aResponse()
                .withStatus(204)));

        AnimeList animeList = client.getAnimeList(username);
        assertThat(animeList,is(nullValue()));
    }

    @Test
    public void verifyCredentials_verificationFailed() throws Exception {
        malService.stubFor(get(urlPathEqualTo(PATH_VERIFY_CREDENTIALS)).willReturn(aResponse()
                .withStatus(204)));

        User user = client.verifyCredentials();
        assertThat(user,is(nullValue()));
    }

    @Test
    public void getAnimeList_withoutUsername_usesAuthenticatedUser() throws Exception {
        String url = String.format("%s?u=%s&type=anime&status=all",PATH_MALAPPINFO,username);
        malService.stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withStatus(200)));
        client.getAnimeList();
        malService.verify(getRequestedFor(urlEqualTo(url)));
    }

    @Test
    public void getMangaList_noResults() throws Exception {
        malService.stubFor(get(urlPathEqualTo(PATH_MALAPPINFO)).willReturn(aResponse()
                .withStatus(204)));

        MangaList mangaList = client.getMangaList(username);
        assertThat(mangaList,is(nullValue()));
    }

    @Test
    public void getMangaList_withoutUsername_usesAuthenticatedUser() throws Exception {
        String url = String.format("%s?u=%s&type=manga&status=all",PATH_MALAPPINFO,username);
        malService.stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withStatus(200)));
        client.getMangaList();
        malService.verify(getRequestedFor(urlEqualTo(url)));
    }

    @Test
    public void verifyCredentials_correctlyUnmarshallsObjects() throws Exception {
        UserXmlBuilder userXml = userXmlBuilder()
                .withId(RandomStringUtils.randomAlphanumeric(16))
                .withUsername(RandomStringUtils.randomAlphanumeric(8));

        malService.stubFor(get(urlPathEqualTo(PATH_VERIFY_CREDENTIALS)).willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_XML)
                .withBody(userXml.build())));

        User user = client.verifyCredentials();
        assertThat(user.getId(),is(userXml.getId()));
        assertThat(user.getUsername(),is(userXml.getUsername()));
    }

    @Test
    public void animeSearch_correctlyUnmarshallsObjects() throws Exception {
        AnimeXmlBuilder animeXml = animeXmlBuilder().withDefaultValues()
                .withSynonyms("AAA;BBB")
                .withType(AnimeType.MOVIE.getValue())
                .withStatus(AnimeStatus.FINISHED_AIRING.getValue())
                .withStartDate("2006-01-07")
                .withEndDate("2006-05-13");
        AnimeSearchResultsXmlBuilder searchResultXml = animeSearchResultsXmlBuilder().withEntry(animeXml.build());

        malService.stubFor(get(urlPathEqualTo(PATH_ANIME_SEARCH)).willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_XML)
                        .withBody(searchResultXml.build())));

        Anime anime = client.searchForAnime(query).get(0);

        assertThat(anime.getId(),is(animeXml.getId()));
        assertThat(anime.getTitle(),is(animeXml.getTitle()));
        assertThat(anime.getEnglishTitle(),is(animeXml.getEnglish()));

        assertThat(anime.getSynonyms(),containsInAnyOrder("AAA","BBB"));
        assertThat(anime.getEpisodes(),is(Integer.parseInt(animeXml.getEpisodes())));
        assertThat(anime.getScore(),is(Float.parseFloat(animeXml.getScore())));
        assertThat(anime.getType(),is(AnimeType.MOVIE));
        assertThat(anime.getStatus(),is(AnimeStatus.FINISHED_AIRING));
        assertThat(anime.getStartDate(),is(dateFromString("2006-01-07")));
        assertThat(anime.getEndDate(),is(dateFromString("2006-05-13")));
        assertThat(anime.getSynopsis(),is(animeXml.getSynopsis()));
        assertThat(anime.getImageUrl(),is(animeXml.getImage()));
    }

    @Test
    public void mangaSearch_correctlyUnmarshallsObjects() throws Exception {
        MangaXmlBuilder mangaXml = mangaXmlBuilder().withDefaultValues()
                .withSynonyms("AAA;BBB")
                .withType(MangaType.MANHWA.getValue())
                .withStatus(MangaStatus.FINISHED.getValue())
                .withStartDate("2006-01-07")
                .withEndDate("2006-05-13");
        MangaSearchResultsXmlBuilder searchResultXml = mangaSearchResultsXmlBuilder().withEntry(mangaXml.build());

        malService.stubFor(get(urlPathEqualTo(PATH_MANGA_SEARCH)).willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_XML)
                .withBody(searchResultXml.build())));

        Manga manga = client.searchForManga(query).get(0);

        assertThat(manga.getId(),is(mangaXml.getId()));
        assertThat(manga.getTitle(),is(mangaXml.getTitle()));
        assertThat(manga.getEnglishTitle(),is(mangaXml.getEnglish()));

        assertThat(manga.getSynonyms(),containsInAnyOrder("AAA","BBB"));
        assertThat(manga.getChapters(),is(Integer.parseInt(mangaXml.getChapters())));
        assertThat(manga.getVolumes(),is(Integer.parseInt(mangaXml.getVolumes())));
        assertThat(manga.getScore(),is(Float.parseFloat(mangaXml.getScore())));
        assertThat(manga.getType(),is(MangaType.MANHWA));
        assertThat(manga.getStatus(),is(MangaStatus.FINISHED));
        assertThat(manga.getStartDate(),is(dateFromString("2006-01-07")));
        assertThat(manga.getEndDate(),is(dateFromString("2006-05-13")));
        assertThat(manga.getSynopsis(),is(mangaXml.getSynopsis()));
        assertThat(manga.getImageUrl(),is(mangaXml.getImage()));
    }

    @Test
    public void getAnimeList_correctlyUnmarshallsObjects() throws Exception {
        AnimeListMyInfoXmlBuilder myInfoXml = animeListMyInfoXmlBuilder().withDefaultValues();
        AnimeListEntryXmlBuilder entryXml = animeListEntryXmlBuilder().withDefaultValues()
                .withSeriesSynonyms("AAA; BBB")
                .withSeriesType(AnimeListSeriesType.ONA.getValue())
                .withSeriesStatus(AnimeListSeriesStatus.NOT_YET_AIRED.getValue())
                .withSeriesStart("2006-06-13")
                .withSeriesEnd("2006-09-22")
                .withMyStartDate("2016-06-13")
                .withMyFinishDate("2016-09-22")
                .withMyStatus(AnimeListEntryStatus.DROPPED.getValue())
                .withMyRewatching("1")
                .withMyTags("XXX, YYY");
        AnimeListXmlBuilder animeListXml = animeListXmlBuilder().withMyInfo(myInfoXml.build()).withEntry(entryXml.build());

        malService.stubFor(get(urlPathEqualTo(PATH_MALAPPINFO)).willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_XML)
                .withBody(animeListXml.build())));

        AnimeList animeList = client.getAnimeList(username);
        AnimeListInfo info = animeList.getListInfo();
        AnimeListEntry entry = animeList.getEntries().get(0);

        assertThat(info.getUserId(),is(myInfoXml.getUserId()));
        assertThat(info.getUserName(),is(myInfoXml.getUserName()));
        assertThat(info.getWatching(),is(Integer.parseInt(myInfoXml.getWatching())));
        assertThat(info.getCompleted(),is(Integer.parseInt(myInfoXml.getCompleted())));
        assertThat(info.getOnHold(),is(Integer.parseInt(myInfoXml.getOnHold())));
        assertThat(info.getDropped(),is(Integer.parseInt(myInfoXml.getDropped())));
        assertThat(info.getPlanToWatch(),is(Integer.parseInt(myInfoXml.getPlanToWatch())));
        assertThat(info.getDaysSpendWatching(),is(Float.parseFloat(myInfoXml.getDaysSpentWatching())));

        assertThat(entry.getSeriesId(),is(entryXml.getSeriesId()));
        assertThat(entry.getSeriesTitle(),is(entryXml.getSeriesTitle()));
        assertThat(entry.getSeriesSynonyms(),is(containsInAnyOrder("AAA","BBB")));
        assertThat(entry.getSeriesType(),is(AnimeListSeriesType.ONA));
        assertThat(entry.getSeriesEpisodes(),is(Integer.parseInt(entryXml.getSeriesEpisodes())));
        assertThat(entry.getSeriesStatus(),is(AnimeListSeriesStatus.NOT_YET_AIRED));
        assertThat(entry.getSeriesStart(),is(dateFromString("2006-06-13")));
        assertThat(entry.getSeriesEnd(),is(dateFromString("2006-09-22")));
        assertThat(entry.getSeriesImageUrl(),is(entryXml.getSeriesImage()));

        assertThat(entry.getEntryId(),is(entryXml.getMyId()));
        assertThat(entry.getWatchedEpisodes(),is(Integer.parseInt(entryXml.getMyWatchedEpisodes())));
        assertThat(entry.getStartedWatching(),is(dateFromString("2016-06-13")));
        assertThat(entry.getFinishedWatching(),is(dateFromString("2016-09-22")));
        assertThat(entry.getUserScore(),is(Integer.parseInt(entryXml.getMyScore())));
        assertThat(entry.getStatus(),is(AnimeListEntryStatus.DROPPED));
        assertThat(entry.getRewatching(),is(true));
        assertThat(entry.getRewatchingEpisodes(),is(Integer.parseInt(entryXml.getMyRewatchingEp())));
        assertThat(entry.getLastUpdated(),is(dateFromEpochSeconds(Long.parseLong(entryXml.getMyLastUpdated()))));
        assertThat(entry.getTags(),is(containsInAnyOrder("XXX","YYY")));
    }

    @Test
    public void getMangaList_correctlyUnmarshallsObjects() throws Exception {
        MangaListMyInfoXmlBuilder myInfoXml = mangaListMyInfoXmlBuilder().withDefaultValues();
        MangaListEntryXmlBuilder entryXml = mangaListEntryXmlBuilder().withDefaultValues()
                .withSeriesSynonyms("AAA; BBB")
                .withSeriesType(MangaListSeriesType.MANGA.getValue())
                .withSeriesStatus(MangaListSeriesStatus.PUBLISHING.getValue())
                .withSeriesStart("2006-06-13")
                .withSeriesEnd("2006-09-22")
                .withMyStartDate("2016-06-13")
                .withMyFinishDate("2016-09-22")
                .withMyStatus(MangaListEntryStatus.DROPPED.getValue())
                .withMyRereading("1")
                .withMyTags("XXX, YYY");
        MangaListXmlBuilder mangaListXml = mangaListXmlBuilder().withMyInfo(myInfoXml.build()).withEntry(entryXml.build());

        malService.stubFor(get(urlPathEqualTo(PATH_MALAPPINFO)).willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_XML)
                .withBody(mangaListXml.build())));

        MangaList mangaList = client.getMangaList(username);
        MangaListInfo info = mangaList.getListInfo();
        MangaListEntry entry = mangaList.getEntries().get(0);

        assertThat(info.getUserId(),is(myInfoXml.getUserId()));
        assertThat(info.getUserName(),is(myInfoXml.getUserName()));
        assertThat(info.getReading(),is(Integer.parseInt(myInfoXml.getReading())));
        assertThat(info.getCompleted(),is(Integer.parseInt(myInfoXml.getCompleted())));
        assertThat(info.getOnHold(),is(Integer.parseInt(myInfoXml.getOnHold())));
        assertThat(info.getDropped(),is(Integer.parseInt(myInfoXml.getDropped())));
        assertThat(info.getPlanToRead(),is(Integer.parseInt(myInfoXml.getPlanToRead())));
        assertThat(info.getDaysSpendReading(),is(Float.parseFloat(myInfoXml.getDaysSpentReading())));

        assertThat(entry.getSeriesId(),is(entryXml.getSeriesId()));
        assertThat(entry.getSeriesTitle(),is(entryXml.getSeriesTitle()));
        assertThat(entry.getSeriesSynonyms(),is(containsInAnyOrder("AAA","BBB")));
        assertThat(entry.getSeriesType(),is(MangaListSeriesType.MANGA));
        assertThat(entry.getSeriesChapters(),is(Integer.parseInt(entryXml.getSeriesChapters())));
        assertThat(entry.getSeriesVolumes(),is(Integer.parseInt(entryXml.getSeriesVolumes())));
        assertThat(entry.getSeriesStatus(),is(MangaListSeriesStatus.PUBLISHING));
        assertThat(entry.getSeriesStart(),is(dateFromString("2006-06-13")));
        assertThat(entry.getSeriesEnd(),is(dateFromString("2006-09-22")));
        assertThat(entry.getSeriesImageUrl(),is(entryXml.getSeriesImage()));

        assertThat(entry.getEntryId(),is(entryXml.getMyId()));
        assertThat(entry.getReadChapters(),is(Integer.parseInt(entryXml.getMyReadChapters())));
        assertThat(entry.getReadVolumes(),is(Integer.parseInt(entryXml.getMyReadVolumes())));
        assertThat(entry.getStartedReading(),is(dateFromString("2016-06-13")));
        assertThat(entry.getFinishedReading(),is(dateFromString("2016-09-22")));
        assertThat(entry.getUserScore(),is(Integer.parseInt(entryXml.getMyScore())));
        assertThat(entry.getStatus(),is(MangaListEntryStatus.DROPPED));
        assertThat(entry.getRereading(),is(true));
        assertThat(entry.getRereadingChapters(),is(Integer.parseInt(entryXml.getMyRereadingChap())));
        assertThat(entry.getLastUpdated(),is(dateFromEpochSeconds(Long.parseLong(entryXml.getMyLastUpdated()))));
        assertThat(entry.getTags(),is(containsInAnyOrder("XXX","YYY")));
    }

    @Test
    public void addToAnimeList_correctlyMarshallsObjects() throws Exception {
        List<String> body = new ArrayList<>();

        malService.stubFor(get(urlPathEqualTo(PATH_ANIME_ADD)).willReturn(aResponse().withStatus(200)));
        malService.addMockServiceRequestListener((request, response) -> body.add(extractBody(request)));

        AnimeListEntryValues values = createTestAnimeListEntryValues();
        client.addToAnimeList("1",values);

        String xml = unifyXml(body.get(0));
        String expectedXml = unifyXml(createTestAnimeListEntryValuesXml());
        assertThat(xml,is(expectedXml));
    }

    @Test
    public void addToMangaList_correctlyMarshallsObjects() throws Exception {
        List<String> body = new ArrayList<>();

        malService.stubFor(get(urlPathEqualTo(PATH_MANGA_ADD)).willReturn(aResponse().withStatus(200)));
        malService.addMockServiceRequestListener((request, response) -> body.add(extractBody(request)));

        MangaListEntryValues values = createTestMangaListEntryValues();
        client.addToMangaList("1",values);

        String xml = unifyXml(body.get(0));
        String expectedXml = unifyXml(createTestMangaListEntryValuesXml());
        assertThat(xml,is(expectedXml));
    }

    @Test
    public void updateAnimeList_correctlyMarshallsObjects() throws Exception {
        List<String> body = new ArrayList<>();

        malService.stubFor(post(urlPathEqualTo(PATH_ANIME_ADD)).willReturn(aResponse().withStatus(200)));
        malService.addMockServiceRequestListener((request, response) -> body.add(extractBody(request)));

        AnimeListEntryValues values = createTestAnimeListEntryValues();
        client.updateAnimeList("1",values);

        String xml = unifyXml(body.get(0));
        String expectedXml = unifyXml(createTestAnimeListEntryValuesXml());
        assertThat(xml,is(expectedXml));
    }

    @Test
    public void updateMangaList_correctlyMarshallsObjects() throws Exception {
        List<String> body = new ArrayList<>();

        malService.stubFor(post(urlPathEqualTo(PATH_MANGA_UPDATE)).willReturn(aResponse().withStatus(200)));
        malService.addMockServiceRequestListener((request, response) -> body.add(extractBody(request)));

       MangaListEntryValues values = createTestMangaListEntryValues();
        client.updateMangaList("1",values);

        String xml = unifyXml(body.get(0));
        String expectedXml = unifyXml(createTestMangaListEntryValuesXml());
        assertThat(xml,is(expectedXml));
    }

    private String extractBody(Request request){
        try {
            return URLDecoder.decode(request.getBodyAsString(),"UTF-8").replace("data=","");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}