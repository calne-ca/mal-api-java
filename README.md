## Java Client for MyAnimeList API

### Usage

**Initializing the Client**

```java
MALClient client = new MALClient("username","password");
```

**Verify Credentials**
```java
try {
    User user = client.verifyCredentials();
    System.out.println("Verified user with id: " + user.getId());
} catch(NotAuthorizedException e) {
    System.err.println("The provided credentials are invalid!");
}
```

**Searching for Anime**
```java
List<Anime> animes = client.searchForAnime("Fate Kaleid");
animes.forEach(a -> System.out.println(a.getTitle()))
```

**Searching for Manga**
```java
List<Manga> mangas = client.searchForManga("Fate Zero");
mangas.forEach(m -> System.out.println(m.getTitle()))
```

**Fetching AnimeList**
```java
AnimeList animeList = client.getAnimeList();
List<AnimeEntry> entries = animeList.getEntries();
entries.forEach(e -> System.out.println(e.getSeriesTitle()))
```

**Fetching MangaList**
```java
MangaList mangaList = client.getMangaList();
List<MangaEntry> entries = mangaList.getEntries();
entries.forEach(e -> System.out.println(e.getSeriesTitle()))
```

**Adding Anime to AnimeList**
```java
AnimeListEntryValues values = new AnimeListEntryValues();
values.setStatus(AnimeListEntryStatus.WATCHING);
values.setEpisode(3);

client.addToAnimeList(anime,values);
```

**Adding Manga to MangaList**
```java
MangaListEntryValues values = new MangaListEntryValues();
values.setStatus(MangaListEntryStatus.READING);
values.setChapter(14);
values.setVolume(1);

client.addToMangaList(manga,values);
```

**Updating AnimeList**
```java
AnimeListEntryValues values = AnimeListEntryValues.from(entry);
values.setStatus(AnimeListEntryStatus.COMPLETED);

client.updateAnimeList(entry,values);
```

**Updating MangaList**
```java
MangaListEntryValues values = MangaListEntryValues.from(entry);
values.setStatus(MangaListEntryStatus.COMPLETED);

client.updateMangaList(entry,values);
```

**Removing Anime from AnimeList**
```java
client.removeFromAnimeList(entry);
```

**Removing Manga from MangaList**
```java
client.removeFromMangaList(entry);
```

### Maven Dependency
```xml
<dependency>
    <groupId>net.beardbot</groupId>
    <artifactId>mal-api</artifactId>
    <version>1.0.1</version>
</dependency>
```