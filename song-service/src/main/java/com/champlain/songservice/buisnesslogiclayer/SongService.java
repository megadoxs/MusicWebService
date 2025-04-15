package com.champlain.songservice.buisnesslogiclayer;

import com.champlain.songservice.presentationlayer.SongRequestModel;
import com.champlain.songservice.presentationlayer.SongResponseModel;

import java.util.List;

public interface SongService {
    List<SongResponseModel> getAllSongs();

    SongResponseModel getSongById(String songId);

    SongResponseModel addSong(SongRequestModel songRequestModel);

    SongResponseModel updateSong(SongRequestModel songRequestModel, String songId);

    void deleteSong(String songId);
}
