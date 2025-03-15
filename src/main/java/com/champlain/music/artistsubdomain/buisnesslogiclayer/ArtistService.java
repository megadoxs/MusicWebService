package com.champlain.music.artistsubdomain.buisnesslogiclayer;

import com.champlain.music.artistsubdomain.presentationlayer.ArtistRequestModel;
import com.champlain.music.artistsubdomain.presentationlayer.ArtistResponseModel;

import java.util.List;

public interface ArtistService {
    List<ArtistResponseModel> getAllArtists();
    ArtistResponseModel getArtistById(String artistId);
    ArtistResponseModel addArtist(ArtistRequestModel artist);
    ArtistResponseModel updateArtist(ArtistRequestModel artist, String artistId);
    void deleteArtist(String artistId);
}
