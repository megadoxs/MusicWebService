package com.champlain.music.songsubdomain.buisnesslogiclayer;

import com.champlain.music.songsubdomain.presentationlayer.SongRequestModel;
import com.champlain.music.songsubdomain.presentationlayer.SongResponseModel;

import java.util.List;

public interface SongService {
    List<SongResponseModel> getAllSongs();

    SongResponseModel getSongById(String songId);

    SongResponseModel addSong(SongRequestModel songRequestModel);

    SongResponseModel updateSong(SongRequestModel songRequestModel, String songId);

    void deleteSong(String songId);
}
