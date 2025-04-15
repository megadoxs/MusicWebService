package com.champlain.songservice.buisnesslogiclayer;

import com.champlain.songservice.dataaccesslayer.Song;
import com.champlain.songservice.dataaccesslayer.SongIdentifier;
import com.champlain.songservice.dataaccesslayer.SongRepository;
import com.champlain.songservice.datamapperlayer.SongRequestModelMapper;
import com.champlain.songservice.datamapperlayer.SongResponseModelMapper;
import com.champlain.songservice.domainclientlayer.ArtistServiceClient;
import com.champlain.songservice.presentationlayer.SongRequestModel;
import com.champlain.songservice.presentationlayer.SongResponseModel;
import com.champlain.songservice.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final SongRequestModelMapper songRequestModelMapper;
    private final SongResponseModelMapper songResponseModelMapper;
    private final ArtistServiceClient artistServiceClient;

    public SongServiceImpl(SongRepository songRepository, SongRequestModelMapper songRequestModelMapper, SongResponseModelMapper songResponseModelMapper, ArtistServiceClient artistServiceClient) {
        this.songRepository = songRepository;
        this.songRequestModelMapper = songRequestModelMapper;
        this.songResponseModelMapper = songResponseModelMapper;
        this.artistServiceClient = artistServiceClient;
    }

    @Override
    public List<SongResponseModel> getAllSongs() {
        List<Song> songs = songRepository.findAll();
        List<SongResponseModel> songResponseModels = songResponseModelMapper.entityToResponseModelList(songs);
        songResponseModels.forEach(song -> song.setArtists(
                songs.stream()
                        .filter(s -> s.getIdentifier().getSongId().equals(song.getIdentifier()))
                        .findAny()
                        .map(s -> s.getArtists().stream().map(artistServiceClient::getArtistById).toList())
                        .orElse(List.of())
        ));
        return songResponseModels;
    }

    @Override
    public SongResponseModel getSongById(String songId) {
        Song song = songRepository.findSongByIdentifier_SongId(songId);
        SongResponseModel songResponseModel = songResponseModelMapper.entityToResponseModel(song);
        addArtist(songResponseModel, song);
        return songResponseModel;
    }

    @Override
    public SongResponseModel addSong(SongRequestModel songRequestModel) {
        Song song = songRequestModelMapper.requestModelToEntity(songRequestModel);
        for (String artistIdentifier : song.getArtists()) {
            if (artistServiceClient.getArtistById(artistIdentifier) == null)
                throw new NotFoundException("artist with id " + artistIdentifier + " was not found");
        }
        song.setIdentifier(new SongIdentifier());
        SongResponseModel songResponseModel = songResponseModelMapper.entityToResponseModel(songRepository.save(song));
        addArtist(songResponseModel, song);
        return songResponseModel;
    }

    @Override
    public SongResponseModel updateSong(SongRequestModel songRequestModel, String songId) {
        Song oldSong = songRepository.findSongByIdentifier_SongId(songId);
        if (oldSong != null) {
            Song song = songRequestModelMapper.requestModelToEntity(songRequestModel);
            for (String artistIdentifier : song.getArtists()) {
                if (artistServiceClient.getArtistById(artistIdentifier) == null)
                    throw new NotFoundException("artist with id " + artistIdentifier + " was not found");
            }
            song.setIdentifier(new SongIdentifier(songId));
            song.setId(oldSong.getId());
            SongResponseModel songResponseModel = songResponseModelMapper.entityToResponseModel(songRepository.save(song));
            addArtist(songResponseModel, song);
            return songResponseModel;
        } else
            throw new NotFoundException("song with id " + songId + " was not found");
    }

    @Override
    public void deleteSong(String songId) {
        Song song = songRepository.findSongByIdentifier_SongId(songId);
        if (song != null) {
            songRepository.delete(song);
        } else
            throw new NotFoundException("song with id " + songId + " was not found");
    }

    public void addArtist(SongResponseModel songResponseModel, Song song) {
        songResponseModel.setArtists(song.getArtists().stream().map(artistServiceClient::getArtistById).toList());
    }
}
