package com.champlain.artistservice.buisnesslogiclayer;

import com.champlain.artistservice.dataaccesslayer.Artist;
import com.champlain.artistservice.dataaccesslayer.ArtistIdentifier;
import com.champlain.artistservice.dataaccesslayer.ArtistRepository;
import com.champlain.artistservice.datamapperlayer.ArtistRequestModelMapper;
import com.champlain.artistservice.datamapperlayer.ArtistResponseModelMapper;
import com.champlain.artistservice.presentationlayer.ArtistRequestModel;
import com.champlain.artistservice.presentationlayer.ArtistResponseModel;
import com.champlain.artistservice.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistRequestModelMapper artistRequestModelMapper;
    private final ArtistResponseModelMapper artistResponseModelMapper;

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistRequestModelMapper artistRequestModelMapper, ArtistResponseModelMapper artistResponseModelMapper) {
        this.artistRepository = artistRepository;
        this.artistRequestModelMapper = artistRequestModelMapper;
        this.artistResponseModelMapper = artistResponseModelMapper;
    }

    @Override
    public List<ArtistResponseModel> getAllArtists() {
        return artistResponseModelMapper.entityToResponseModelList(artistRepository.findAll());
    }

    @Override
    public ArtistResponseModel getArtistById(String artistId) {
        return artistResponseModelMapper.entityToResponseModel(artistRepository.findArtistByIdentifier_ArtistId(artistId));
    }

    @Override
    public ArtistResponseModel addArtist(ArtistRequestModel artistRequestModel) {
        Artist artist = artistRequestModelMapper.requestModelToEntity(artistRequestModel);
        artist.setIdentifier(new ArtistIdentifier());
        return artistResponseModelMapper.entityToResponseModel(artistRepository.save(artist));
    }

    @Override
    public ArtistResponseModel updateArtist(ArtistRequestModel artistRequestModel, String artistId) {
        Artist oldArtist = artistRepository.findArtistByIdentifier_ArtistId(artistId);
        if (oldArtist != null) {
            Artist artist = artistRequestModelMapper.requestModelToEntity(artistRequestModel);
            artist.setIdentifier(new ArtistIdentifier(artistId));
            artist.setId(oldArtist.getId());
            return artistResponseModelMapper.entityToResponseModel(artistRepository.save(artist));
        } else
            throw new NotFoundException("artist with id " + artistId + " was not found");
    }

    @Override
    public void deleteArtist(String artistId) {
        Artist artist = artistRepository.findArtistByIdentifier_ArtistId(artistId);
        if (artist != null) {
            artistRepository.delete(artist);
        } else
            throw new NotFoundException("artist with id " + artistId + " was not found");
    }
}
