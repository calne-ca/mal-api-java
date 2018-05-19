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

import java.util.Collections;
import java.util.List;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE;
import static javax.ws.rs.core.MediaType.WILDCARD_TYPE;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import lombok.NonNull;
import net.beardbot.myanimelist.model.User;
import net.beardbot.myanimelist.model.anime.*;
import net.beardbot.myanimelist.model.manga.*;
import static net.beardbot.myanimelist.MAL.*;
import static net.beardbot.myanimelist.utils.XmlUtils.*;

/**
 * A client for the documented and undocumented parts of the official MyAnimeList API.
 */
public class MALClient implements AutoCloseable {

    private Client client;

    private final ClientConfig clientConfig;
    private final String malUrl;
    private final String username;

    /**
     * Create a new instance of the MALClient from the given credentials.
     * <p>
     * MyAnimeList is hosted behind TLS, therefore username and password will be encrypted
     * before sending them through the internet.
     *
     * @param username {@code [required]} Username of the MAL user.
     * @param password {@code [required]} Password of the MAL user.
     * @throws NullPointerException If any of the parameters are null.
     */
    public MALClient(
            @NonNull final String username,
            @NonNull final String password) {

        this(username, password, MAL_URI);
    }

    MALClient(
            @NonNull final String username,
            @NonNull final String password,
            @NonNull final String malUrl) {

        this.username = username;
        this.malUrl = malUrl;
        this.clientConfig = createClientConfig(username,password);
        this.client = ClientBuilder.newClient(clientConfig);
    }

    /**
     * Execute an anime search query against MAL.
     * <p>
     *
     * @param query {@code [required]} The query to run against MAL. Example: "Fate Kaleid"
     * @return A list of {@link Anime} matching the query. Can be empty but not {@code null}.
     * @throws NullPointerException             If the query is not provided.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the cerdentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public List<Anime> searchForAnime(
            @NonNull final String query) {

        Response response = client.target(malUrl)
                           .path(PATH_ANIME_SEARCH)
                           .queryParam("q", query)
                           .request(APPLICATION_XML_TYPE)
                           .get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
            return Collections.emptyList();
        }

        return response.readEntity(new GenericType<List<Anime>>(){});
    }

    /**
     * Execute a manga search query against MAL.
     * <p>
     *
     * @param query {@code [required]} The query to run against MAL. Example: "Fate Zero"
     * @return A list of {@link Manga} matching the query. Can be empty but not {@code null}.
     * @throws NullPointerException             If the query is not provided.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public List<Manga> searchForManga(
            @NonNull final String query) {

        Response response = client.target(malUrl)
                                  .path(PATH_MANGA_SEARCH)
                                  .queryParam("q", query)
                                  .request(APPLICATION_XML_TYPE)
                                  .get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
            return Collections.emptyList();
        }

        return response.readEntity(new GenericType<List<Manga>>(){});
    }

    /**
     * Adds an anime to the anime list.
     * <p>
     *
     * @param anime {@code [required]} The {@link Anime} you want to add the anime list.
     * @param values {@code [required]} An {@link AnimeListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void addToAnimeList(
            @NonNull final Anime anime,
            @NonNull final AnimeListEntryValues values) {

        addToAnimeList(anime.getId(),values);
    }

    /**
     * Adds an anime to the anime list.
     * <p>
     *
     * @param animeId {@code [required]} The ID of the anime you want to add the anime list.
     * @param values {@code [required]} An {@link AnimeListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void addToAnimeList(
            @NonNull final String animeId,
            @NonNull final AnimeListEntryValues values) {

        final Form form = new Form();
        form.param("data", objectToXml(values));

        Response response = client.target(malUrl)
                              .path(PATH_ANIME_ADD.replace("%id", animeId))
                              .request(WILDCARD_TYPE)
                              .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
        }
    }

    /**
     * Adds an manga to the manga list.
     * <p>
     *
     * @param manga {@code [required]} The {@link Manga} you want to add the manga list.
     * @param values {@code [required]} An {@link MangaListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void addToMangaList(
            @NonNull final Manga manga,
            @NonNull final MangaListEntryValues values) {

        addToMangaList(manga.getId(),values);
    }

    /**
     * Adds an manga to the manga list.
     * <p>
     *
     * @param mangaId {@code [required]} The ID of the manga you want to add the manga list.
     * @param values {@code [required]} An {@link MangaListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void addToMangaList(
            @NonNull final String mangaId,
            @NonNull final MangaListEntryValues values) {

        final Form form = new Form();
        form.param("data", objectToXml(values));

        Response response = client.target(malUrl)
                              .path(PATH_MANGA_ADD.replace("%id", mangaId))
                              .request(WILDCARD_TYPE)
                              .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
        }
    }

    /**
     * Updates an anime on the anime list.
     * <p>
     *
     * @param anime {@code [required]} The {@link Anime} whose entry you want to update.
     * @param values {@code [required]} An {@link AnimeListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void updateAnimeList(
            @NonNull final Anime anime,
            @NonNull final AnimeListEntryValues values) {

        updateAnimeList(anime.getId(),values);
    }

    /**
     * Updates an anime on the anime list.
     * <p>
     *
     * @param entry {@code [required]} The {@link AnimeListEntry} you want to update.
     * @param values {@code [required]} An {@link AnimeListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void updateAnimeList(
            @NonNull final AnimeListEntry entry,
            @NonNull final AnimeListEntryValues values) {

        updateAnimeList(entry.getSeriesId(),values);
    }

    /**
     * Updates an anime on the anime list.
     * <p>
     *
     * @param animeId {@code [required]} The ID of the anime you want to update.
     * @param values {@code [required]} An {@link AnimeListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void updateAnimeList(
            @NonNull final String animeId,
            @NonNull final AnimeListEntryValues values) {

        final Form form = new Form();
        form.param("data", objectToXml(values));

        Response response = client.target(malUrl)
                              .path(PATH_ANIME_UPDATE.replace("%id", animeId))
                              .request(WILDCARD_TYPE)
                              .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
        }
    }

    /**
     * Updates an manga on the manga list.
     * <p>
     *
     * @param manga {@code [required]} The {@link Manga} whose entry you want to update.
     * @param values {@code [required]} An {@link MangaListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void updateMangaList(
            @NonNull final Manga manga,
            @NonNull final MangaListEntryValues values) {

        updateMangaList(manga.getId(),values);
    }

    /**
     * Updates an manga on the manga list.
     * <p>
     *
     * @param entry {@code [required]} The {@link MangaListEntry} you want to update.
     * @param values {@code [required]} An {@link MangaListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void updateMangaList(
            @NonNull final MangaListEntry entry,
            @NonNull final MangaListEntryValues values) {

        updateMangaList(entry.getSeriesId(),values);
    }

    /**
     * Updates an manga on the manga list.
     * <p>
     *
     * @param mangaId {@code [required]} The ID of the manga you want to update.
     * @param values {@code [required]} An {@link MangaListEntryValues} object containing information about the entry such as the current status.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void updateMangaList(
            @NonNull final String mangaId,
            @NonNull final MangaListEntryValues values) {

        final Form form = new Form();
        form.param("data", objectToXml(values));

        Response response = client.target(malUrl)
                              .path(PATH_MANGA_UPDATE.replace("%id", mangaId))
                              .request(WILDCARD_TYPE)
                              .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
        }
    }

    /**
     * Removes an anime from the anime list.
     * <p>
     *
     * @param anime {@code [required]} The {@link Anime} whose entry you want to remove from the anime list.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void removeFromAnimeList(
            @NonNull final Anime anime) {

        removeFromAnimeList(anime.getId());
    }

    /**
     * Removes an anime from the anime list.
     * <p>
     *
     * @param entry {@code [required]} The {@link AnimeListEntry} you want to remove from the anime list.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void removeFromAnimeList(
            @NonNull final AnimeListEntry entry) {

        removeFromAnimeList(entry.getSeriesId());
    }

    /**
     * Removes an anime from the anime list.
     * <p>
     *
     * @param animeId {@code [required]} The ID of the anime you want to remove from the anime list.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void removeFromAnimeList(
            @NonNull final String animeId) {

        Response response = client.target(malUrl)
                                .path(PATH_ANIME_DELETE.replace("%id", animeId))
                                .request(WILDCARD_TYPE)
                                .delete();

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
        }
    }

    /**
     * Removes an manga from the manga list.
     * <p>
     *
     * @param manga {@code [required]} The {@link Manga} whose entry you want to remove from the manga list.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void removeFromMangaList(
            @NonNull final Manga manga) {

        removeFromMangaList(manga.getId());
    }

    /**
     * Removes an manga from the manga list.
     * <p>
     *
     * @param entry {@code [required]} The {@link MangaListEntry} you want to remove from the manga list.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void removeFromMangaList(
            @NonNull final MangaListEntry entry) {

        removeFromMangaList(entry.getSeriesId());
    }

    /**
     * Removes an manga from the manga list.
     * <p>
     *
     * @param mangaId {@code [required]} The ID of the manga you want to remove from the manga list.
     * @throws NullPointerException             If any of the parameters are null.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public void removeFromMangaList(
            @NonNull final String mangaId) {

        Response response = client.target(malUrl)
                                .path(PATH_MANGA_DELETE.replace("%id", mangaId))
                                .request(WILDCARD_TYPE)
                                .delete();

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
        }
    }

    /**
     * Verifies the credentials provided with the creation of the {@link MALClient}.
     * <p>
     *
     * @return A {@link User} object containing username and user ID.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the credentials provided with this {@link MALClient} are invalid.
     */
    public User verifyCredentials() {
        Response response = client.target(malUrl)
                                  .path(PATH_VERIFY_CREDENTIALS)
                                  .request(APPLICATION_XML_TYPE)
                                  .get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
            return null;
        }

        return response.readEntity(User.class);
    }

    /**
     * Fetches the anime list of the user provided with the creation of the {@link MALClient}.
     * <p>
     *
     * @return An {@link AnimeList} object containing information about the anime list as well as the actual entries.
     * @throws NullPointerException             If the username is not provided.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the cerdentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public AnimeList getAnimeList() {
        return getAnimeList(this.username);
    }

    /**
     * Fetches the anime list of a given usern.
     * <p>
     *
     * @param username The username of the user whose anime list shall be fetched.
     * @return An {@link AnimeList} object containing information about the anime list as well as the actual entries.
     * @throws NullPointerException             If the username is not provided.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the cerdentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public AnimeList getAnimeList(
            @NonNull final String username) {

        Response response = client.target(malUrl)
                .path(PATH_MALAPPINFO)
                .queryParam("u", username)
                .queryParam("type", "anime")
                .queryParam("status", "all")
                .request(APPLICATION_XML_TYPE)
                .get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
            return null;
        }

        AnimeList result = response.readEntity(AnimeList.class);

        if (result == null || result.getListInfo() == null){
            return null;
        }

        return result;
    }

    /**
     * Fetches the manga list of the user provided with the creation of the {@link MALClient}.
     * <p>
     *
     * @return A {@link MangaList} object containing information about the manga list as well as the actual entries.
     * @throws NullPointerException             If the username is not provided.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the cerdentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public MangaList getMangaList() {
        return getMangaList(this.username);
    }

    /**
     * Fetches the anime list of a given usern.
     * <p>
     *
     * @param username {@code [required]} The username of the user whose manga list shall be fetched.
     * @return An {@link MangaList} object containing information about the manga list as well as the actual entries.
     * @throws NullPointerException             If the username is not provided.
     * @throws javax.ws.rs.ClientErrorException If MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException If MAL returns a HTTP {@code 5xx} status code.
     * @throws javax.ws.rs.ProcessingException  If the response from MAL cannot be interpreted.
     * @throws javax.ws.rs.NotAuthorizedException  If the cerdentials provided with this {@link MALClient} are invalid.
     *                                             To detect this beforehand use the {@code verifyCredentials} method.
     */
    public MangaList getMangaList(
            @NonNull final String username) {

        Response response = client.target(malUrl)
                .path(PATH_MALAPPINFO)
                .queryParam("u", username)
                .queryParam("type", "manga")
                .queryParam("status", "all")
                .request(APPLICATION_XML_TYPE)
                .get();

        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            handleError(response);
            return null;
        }

        MangaList result = response.readEntity(MangaList.class);

        if (result == null || result.getListInfo() == null){
            return null;
        }

        return result;
    }

    private void handleError(Response response){
        int status = response.getStatus();
        String message = response.readEntity(String.class);

        if (status == Response.Status.NO_CONTENT.getStatusCode()){
            // This is necessary because Jersey cannot handle MAL's 204 response correctly for whatever reason.
            this.client.close();
            this.client = ClientBuilder.newClient(this.clientConfig);
        }
        if (status == Response.Status.UNAUTHORIZED.getStatusCode()){
            throw new NotAuthorizedException(message);
        }
        if (status >= Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()){
            throw new ServerErrorException(message, status);
        }
        if (status >= Response.Status.BAD_REQUEST.getStatusCode()){
            throw new ClientErrorException(message, status);
        }
    }

    private ClientConfig createClientConfig(String username, String password){
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());
        clientConfig.register(HttpAuthenticationFeature.basicBuilder().credentials(username,password).build());
        return clientConfig;
    }

    @Override
    public void close() {
        if (client != null) {
            client.close();
        }
    }
}