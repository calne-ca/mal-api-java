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

class MAL {
    static final String MAL_URI = "https://myanimelist.net";

    static final String PATH_ANIME_SEARCH = "/api/anime/search.xml";
    static final String PATH_ANIME_ADD = "/api/animelist/add/%id.xml";
    static final String PATH_ANIME_UPDATE = "/api/animelist/update/%id.xml";
    static final String PATH_ANIME_DELETE = "/api/animelist/delete/%id.xml";

    static final String PATH_MANGA_SEARCH = "/api/manga/search.xml";
    static final String PATH_MANGA_ADD = "/api/mangalist/add/%id.xml";
    static final String PATH_MANGA_UPDATE = "/api/mangalist/update/%id.xml";
    static final String PATH_MANGA_DELETE = "/api/mangalist/delete/%id.xml";

    static final String PATH_VERIFY_CREDENTIALS = "/api/account/verify_credentials.xml";

    static final String PATH_MALAPPINFO = "/malappinfo.php";
}
