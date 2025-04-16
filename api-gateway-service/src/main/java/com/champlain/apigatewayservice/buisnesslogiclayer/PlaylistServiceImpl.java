package com.champlain.apigatewayservice.buisnesslogiclayer;

import com.champlain.apigatewayservice.domainclientlayer.PlaylistServiceClient;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistServiceClient playlistServiceClient;

    public PlaylistServiceImpl(PlaylistServiceClient playlistServiceClient) {
        this.playlistServiceClient = playlistServiceClient;
    }

    @Override
    public List<PlaylistResponseModel> getPlaylists() {
        return playlistServiceClient.getPlaylists();
    }

    @Override
    public PlaylistResponseModel getPlaylistById(String playlistId) {
        return playlistServiceClient.getPlaylistById(playlistId);
    }

    @Override
    public PlaylistResponseModel addPlaylist(PlaylistRequestModel playlistRequestModel) {
        return playlistServiceClient.addPlaylist(playlistRequestModel);
    }

    @Override
    public PlaylistResponseModel updatePlaylist(PlaylistRequestModel playlistRequestModel, String playlistId) {
        return playlistServiceClient.updatePlaylist(playlistRequestModel, playlistId);
    }

    @Override
    public void deletePlaylist(String playlistId) {
        playlistServiceClient.deletePlaylist(playlistId);
    }
}
