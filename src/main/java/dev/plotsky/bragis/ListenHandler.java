package dev.plotsky.bragis;

import com.wrapper.spotify.SpotifyApi;
import dev.plotsky.bragis.music.Listen;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ListenHandler {
    public Mono<ServerResponse> listen(ServerRequest request) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken("BQCeLxY1Jpxm2dnT4-geQBqHZHG2vga_5WHzh0yw50nRoQfIZim" +
                        "-5y6j7TxZiEMJkzia4avt00APU4uUNt8BXViAycO4poocxllSwz8bhBJ9yKbp" +
                        "-Lvl7bAWfCA7kRjW3yxGjAg0-MpP-TsnM-Kpc1F3qKsJFO3yLtbgfEjO_1YO")
                .build();
        var recentlyPlayedTracks =
                spotifyApi.getCurrentUsersRecentlyPlayedTracks().build().executeAsync();
        var listens = Mono.fromFuture(recentlyPlayedTracks)
                .flatMapMany(cursor -> Flux.fromArray(cursor.getItems()))
                .map(playHistory -> new Listen(playHistory.getTrack().getName()));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(listens, Listen.class);
    }
}
