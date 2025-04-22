package com.champlain.artistservice.buisnesslogiclayer;

import com.champlain.artistservice.dataaccesslayer.Artist;
import com.champlain.artistservice.dataaccesslayer.ArtistIdentifier;
import com.champlain.artistservice.dataaccesslayer.ArtistRepository;
import com.champlain.artistservice.datamapperlayer.ArtistRequestModelMapper;
import com.champlain.artistservice.datamapperlayer.ArtistResponseModelMapper;
import com.champlain.artistservice.domainclientlayer.SongServiceClient;
import com.champlain.artistservice.presentationlayer.ArtistRequestModel;
import com.champlain.artistservice.presentationlayer.ArtistResponseModel;
import com.champlain.artistservice.presentationlayer.SongResponseModel;
import com.champlain.artistservice.utils.exceptions.DuplicateStageNameException;
import com.champlain.artistservice.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistRequestModelMapper artistRequestModelMapper;
    private final ArtistResponseModelMapper artistResponseModelMapper;
    private final SongServiceClient songServiceClient;

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistRequestModelMapper artistRequestModelMapper, ArtistResponseModelMapper artistResponseModelMapper, SongServiceClient songServiceClient) {
        this.artistRepository = artistRepository;
        this.artistRequestModelMapper = artistRequestModelMapper;
        this.artistResponseModelMapper = artistResponseModelMapper;
        this.songServiceClient = songServiceClient;
    }

    @Override
    public List<ArtistResponseModel> getAllArtists() {
        return artistResponseModelMapper.entityToResponseModelList(artistRepository.findAll());
    }

    @Override
    public ArtistResponseModel getArtistById(String artistId) {
        Artist artist = artistRepository.findArtistByIdentifier_ArtistId(artistId);

        if (artist == null)
            throw new NotFoundException("artist with id " + artistId + " was not found");

        return artistResponseModelMapper.entityToResponseModel(artist);
    }

    @Override
    public ArtistResponseModel addArtist(ArtistRequestModel artistRequestModel) {
        Artist artist = artistRequestModelMapper.requestModelToEntity(artistRequestModel);

        if(getAllArtists().stream().anyMatch(a -> a.getStageName().equals(artist.getStageName())))
            throw new DuplicateStageNameException("Stage name " + artist.getStageName() + " already exists.");

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

            if(getAllArtists().stream().anyMatch(a -> a.getStageName().equals(artist.getStageName()) && !a.getIdentifier().equals(artist.getIdentifier().getArtistId())))
                throw new DuplicateStageNameException("Stage name " + artist.getStageName() + " already exists.");

            return artistResponseModelMapper.entityToResponseModel(artistRepository.save(artist));
        } else
            throw new NotFoundException("artist with id " + artistId + " was not found");
    }

    @Override
    public void deleteArtist(String artistId) {
        Artist artist = artistRepository.findArtistByIdentifier_ArtistId(artistId);
        if (artist != null) {
            for (SongResponseModel song : songServiceClient.getSongsByArtistId(artistId)){
                if(song.getArtists().size() == 1)
                    songServiceClient.deleteSongById(song.getIdentifier());
                else if(song.getArtists().stream().anyMatch(s -> s.getIdentifier().equals(artist.getIdentifier().getArtistId()))){
                    songServiceClient.removeArtistFromSong(song, artistId);
                }
            }
            artistRepository.delete(artist);
        } else
            throw new NotFoundException("artist with id " + artistId + " was not found");
    }
}
