package com.champlain.apigatewayservice.buisnesslogiclayer;

import com.champlain.apigatewayservice.domainclientlayer.ArtistServiceClient;
import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {
    private final ArtistServiceClient artistServiceClient;

    public ArtistServiceImpl(ArtistServiceClient artistServiceClient) {
        this.artistServiceClient = artistServiceClient;
    }

    @Override
    public List<ArtistResponseModel> getArtists() {
        return artistServiceClient.getArtists();
    }

    @Override
    public ArtistResponseModel getArtistById(String artistId) {
        return artistServiceClient.getArtistById(artistId);
    }

    @Override
    public ArtistResponseModel addArtist(ArtistRequestModel artistRequestModel) {
        return artistServiceClient.addArtist(artistRequestModel);
    }

    @Override
    public ArtistResponseModel updateArtist(ArtistRequestModel artistRequestModel, String artistId) {
        return artistServiceClient.updateArtist(artistRequestModel, artistId);
    }

    @Override
    public void deleteArtist(String artistId) {
        artistServiceClient.deleteArtist(artistId);
    }
}
