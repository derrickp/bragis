package dev.plotsky.bragis;

import com.wrapper.spotify.SpotifyApi;
import dev.plotsky.bragis.music.Listen;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class ListenHandler {
    public Mono<ServerResponse> listen(ServerRequest request) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken("ACCESS_TOKEN_FROM_SPOTIFY")
                .build();
        var recentlyPlayedTracksRequest = spotifyApi.getCurrentUsersRecentlyPlayedTracks().build();
        var recentlyPlayedTracksFuture = recentlyPlayedTracksRequest.executeAsync();
        var x = Mono.fromFuture(recentlyPlayedTracksFuture)
                .map(cursor -> Arrays.stream(cursor.getItems())
                        .map(playHistory -> new Listen(playHistory.getTrack().getName())).collect(Collectors.toList()));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(x, Listen.class);
    }
}
