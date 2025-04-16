package com.champlain.apigatewayservice.buisnesslogiclayer;

import com.champlain.apigatewayservice.domainclientlayer.SongServiceClient;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongRequestModel;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {
    private final SongServiceClient songServiceClient;

    public SongServiceImpl(SongServiceClient songServiceClient) {
        this.songServiceClient = songServiceClient;
    }

    @Override
    public List<SongResponseModel> getSongs() {
        return songServiceClient.getSongs();
    }

    @Override
    public SongResponseModel getSongById(String songId) {
        return songServiceClient.getSongById(songId);
    }

    @Override
    public SongResponseModel addSong(SongRequestModel songRequestModel) {
        return songServiceClient.addSong(songRequestModel);
    }

    @Override
    public SongResponseModel updateSong(SongRequestModel songRequestModel, String songId) {
        return songServiceClient.updateSong(songRequestModel, songId);
    }

    @Override
    public void deleteSong(String songId) {
        songServiceClient.deleteSong(songId);
    }
}
