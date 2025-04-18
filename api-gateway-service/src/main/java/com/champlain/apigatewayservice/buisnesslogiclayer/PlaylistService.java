package com.champlain.apigatewayservice.buisnesslogiclayer;

import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistResponseModel;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistResponseModel;

import java.util.List;

public interface PlaylistService {
    List<PlaylistResponseModel> getPlaylists();

    PlaylistResponseModel getPlaylistById(String playlistId);

    PlaylistResponseModel addPlaylist(PlaylistRequestModel playlistRequestModel);

    PlaylistResponseModel updatePlaylist(PlaylistRequestModel playlistRequestModel, String playlistId);

    void deletePlaylist(String playlistId);

    List<ArtistResponseModel> getPlaylistArtists(String playlistId);
}
