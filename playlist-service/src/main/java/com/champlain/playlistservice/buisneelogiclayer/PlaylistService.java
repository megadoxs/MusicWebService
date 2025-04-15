package com.champlain.playlistservice.buisneelogiclayer;

import com.champlain.playlistservice.presentationlayer.ArtistResponseModel;
import com.champlain.playlistservice.presentationlayer.PlaylistRequestModel;
import com.champlain.playlistservice.presentationlayer.PlaylistResponseModel;

import java.util.List;

public interface PlaylistService {
    List<PlaylistResponseModel> getAllPlaylists();

    PlaylistResponseModel getPlaylistById(String playlistId);

    PlaylistResponseModel addPlaylist(PlaylistRequestModel songRequestModel);

    PlaylistResponseModel updatePlaylist(PlaylistRequestModel songRequestModel, String playlistId);

    void deletePlaylist(String playlistId);

    List<ArtistResponseModel> getAllArtists(String playlistId);
}
