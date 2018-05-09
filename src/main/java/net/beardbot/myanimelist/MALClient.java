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

import lombok.NonNull;
import net.beardbot.myanimelist.model.User;
import net.beardbot.myanimelist.model.anime.Anime;
import net.beardbot.myanimelist.model.anime.AnimeList;
import net.beardbot.myanimelist.model.anime.AnimeListEntryValues;
import net.beardbot.myanimelist.model.manga.Manga;
import net.beardbot.myanimelist.model.manga.MangaList;
import net.beardbot.myanimelist.model.manga.MangaListEntryValues;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.*;
import static net.beardbot.myanimelist.MAL.*;
import static net.beardbot.myanimelist.utils.XmlUtils.objectToXml;

public class MALClient implements AutoCloseable {

    private final Client client;
    private final String malUrl;
    private final String username;

    public MALClient(
            @NonNull final String username,
            @NonNull final String password) {

        this(username, password, MAL_URI);
    }

    MALClient(
            @NonNull final String username,
            @NonNull final String password,
            @NonNull final String malUrl) {

        Logger logger = Logger.getLogger(getClass().getName());
        Feature loggingFeature = new LoggingFeature(logger, Level.ALL, null, null);

        this.username = username;
        this.client = ClientBuilder.newClient(
                new ClientConfig()
                        .connectorProvider(new ApacheConnectorProvider())
                        .register(HttpAuthenticationFeature.basicBuilder()
                                .credentials(username, password)
                                .build()))
                        .register(loggingFeature);
        this.malUrl = malUrl;
    }

    public List<Anime> searchForAnime(
            @NonNull final String query) {

        final List<Anime> results = client.target(malUrl)
                .path(PATH_ANIME_SEARCH)
                .queryParam("q", query)
                .request(APPLICATION_XML_TYPE)
                .get(new GenericType<List<Anime>>() {});

        if (results == null)
            return emptyList();

        return results;
    }

    public List<Manga> searchForManga(
            @NonNull final String query) {

        final List<Manga> results = client.target(malUrl)
                .path(PATH_MANGA_SEARCH)
                .queryParam("q", query)
                .request(APPLICATION_XML_TYPE)
                .get(new GenericType<List<Manga>>() {});

        if (results == null)
            return emptyList();

        return results;
    }

    public void addToAnimeList(
            @NonNull final String animeId,
            @NonNull final AnimeListEntryValues values) {

        final Form form = new Form();
        form.param("data", objectToXml(values));

        client.target(malUrl)
                .path(PATH_ANIME_ADD.replace("%id", animeId))
                .request(WILDCARD_TYPE)
                .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));
    }

    public void addToMangaList(
            @NonNull final String mangaId,
            @NonNull final MangaListEntryValues values) {

        final Form form = new Form();
        form.param("data", objectToXml(values));

        client.target(malUrl)
                .path(PATH_MANGA_ADD.replace("%id", mangaId))
                .request(WILDCARD_TYPE)
                .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));
    }

    public void updateAnimeList(
            @NonNull final String animeId,
            @NonNull final AnimeListEntryValues values) {

        final Form form = new Form();
        form.param("data", objectToXml(values));

        client.target(malUrl)
                .path(PATH_ANIME_UPDATE.replace("%id", animeId))
                .request(WILDCARD_TYPE)
                .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));
    }

    public void updateMangaList(
            @NonNull final String mangaId,
            @NonNull final MangaListEntryValues values) {

        final Form form = new Form();
        form.param("data", objectToXml(values));

        client.target(malUrl)
                .path(PATH_MANGA_UPDATE.replace("%id", mangaId))
                .request(WILDCARD_TYPE)
                .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));
    }

    public void deleteFromAnimeList(
            @NonNull final String animeId) {

        client.target(malUrl)
                .path(PATH_ANIME_DELETE.replace("%id", animeId))
                .request(WILDCARD_TYPE)
                .delete(String.class);
    }

    public void deleteFromMangaList(
            @NonNull final String mangaId) {

        client.target(malUrl)
                .path(PATH_MANGA_DELETE.replace("%id", mangaId))
                .request(WILDCARD_TYPE)
                .delete(String.class);
    }

    public User verifyCredentials() {
        return client.target(malUrl)
                .path(PATH_VERIFY_CREDENTIALS)
                .request(APPLICATION_XML_TYPE)
                .get(User.class);
    }

    public AnimeList getAnimeList() {
        return getAnimeList(this.username);
    }

    public AnimeList getAnimeList(
            @NonNull final String username) {

        return client.target(malUrl)
                .path(PATH_MALAPPINFO)
                .queryParam("u", username)
                .queryParam("type", "anime")
                .queryParam("status", "all")
                .request(APPLICATION_XML_TYPE)
                .get(AnimeList.class);
    }

    public MangaList getMangaList() {
        return getMangaList(this.username);
    }

    public MangaList getMangaList(
            @NonNull final String username) {

        return client.target(malUrl)
                .path(PATH_MALAPPINFO)
                .queryParam("u", username)
                .queryParam("type", "manga")
                .queryParam("status", "all")
                .request(APPLICATION_XML_TYPE)
                .get(MangaList.class);
    }

    @Override
    public void close() throws Exception {
        if (client != null) {
            client.close();
        }
    }
}