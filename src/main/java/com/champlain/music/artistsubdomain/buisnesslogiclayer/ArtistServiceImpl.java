package com.champlain.music.artistsubdomain.buisnesslogiclayer;

import com.champlain.music.artistsubdomain.dataaccesslayer.Artist;
import com.champlain.music.artistsubdomain.dataaccesslayer.ArtistIdentifier;
import com.champlain.music.artistsubdomain.dataaccesslayer.ArtistRepository;
import com.champlain.music.artistsubdomain.datamapperlayer.ArtistRequestModelMapper;
import com.champlain.music.artistsubdomain.datamapperlayer.ArtistResponseModelMapper;
import com.champlain.music.artistsubdomain.presentationlayer.ArtistRequestModel;
import com.champlain.music.artistsubdomain.presentationlayer.ArtistResponseModel;
import com.champlain.music.utils.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService{
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
        Artist oldArtist = artistRequestModelMapper.requestModelToEntity(artistRequestModel);
        if(oldArtist != null) {
            Artist artist = artistRequestModelMapper.requestModelToEntity(artistRequestModel);
            artist.setIdentifier(new ArtistIdentifier(artistId));
            artist.setId(oldArtist.getId());
            return artistResponseModelMapper.entityToResponseModel(artistRepository.save(artist));
        }
        else
            throw new NotFoundException("artist with id " + artistId + " was not found");
    }

    @Override
    public void deleteArtist(String artistId) {
        Artist artist = artistRepository.findArtistByIdentifier_ArtistId(artistId);
        if (artist != null) {
            artistRepository.delete(artist);
        }
        else
            throw new NotFoundException("artist with id " + artistId + " was not found");
    }
}
