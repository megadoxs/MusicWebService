package com.champlain.artistservice.buisnesslogiclayer;

import com.champlain.artistservice.presentationlayer.ArtistRequestModel;
import com.champlain.artistservice.presentationlayer.ArtistResponseModel;

import java.util.List;

public interface ArtistService {
    List<ArtistResponseModel> getAllArtists();

    ArtistResponseModel getArtistById(String artistId);

    ArtistResponseModel addArtist(ArtistRequestModel artist);

    ArtistResponseModel updateArtist(ArtistRequestModel artist, String artistId);

    void deleteArtist(String artistId);
}
