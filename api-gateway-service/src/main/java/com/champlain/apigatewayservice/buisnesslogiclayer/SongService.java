package com.champlain.apigatewayservice.buisnesslogiclayer;

import com.champlain.apigatewayservice.presentationlayer.songdto.SongRequestModel;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongResponseModel;

import java.util.List;

public interface SongService {
    List<SongResponseModel> getSongs();

    SongResponseModel getSongById(String songId);

    SongResponseModel addSong(SongRequestModel songRequestModel);

    SongResponseModel updateSong(SongRequestModel songRequestModel, String songId);

    void deleteSong(String songId);
}
