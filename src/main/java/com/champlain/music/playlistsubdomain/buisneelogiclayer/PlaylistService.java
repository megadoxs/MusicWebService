package com.champlain.music.playlistsubdomain.buisneelogiclayer;

import com.champlain.music.playlistsubdomain.presentationlayer.ArtistResponseModel;
import com.champlain.music.playlistsubdomain.presentationlayer.PlaylistRequestModel;
import com.champlain.music.playlistsubdomain.presentationlayer.PlaylistResponseModel;

import java.util.List;

public interface PlaylistService {
    List<PlaylistResponseModel> getAllPlaylists();

    PlaylistResponseModel getPlaylistById(String playlistId);

    PlaylistResponseModel addPlaylist(PlaylistRequestModel songRequestModel);

    PlaylistResponseModel updatePlaylist(PlaylistRequestModel songRequestModel, String playlistId);

    void deletePlaylist(String playlistId);

    List<ArtistResponseModel> getAllArtists(String playlistId);
}
