package com.champlain.apigatewayservice.buisnesslogiclayer;

import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistResponseModel;

import java.util.List;

public interface ArtistService {
    List<ArtistResponseModel> getArtists();

    ArtistResponseModel getArtistById(String artistId);

    ArtistResponseModel addArtist(ArtistRequestModel artistRequestModel);

    ArtistResponseModel updateArtist(ArtistRequestModel artistRequestModel, String artistId);

    void deleteArtist(String artistId);
}
