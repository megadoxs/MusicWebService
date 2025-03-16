package com.champlain.music.songsubdomain.buisnesslogiclayer;

import com.champlain.music.songsubdomain.dataaccesslayer.Song;
import com.champlain.music.songsubdomain.dataaccesslayer.SongIdentifier;
import com.champlain.music.songsubdomain.dataaccesslayer.SongRepository;
import com.champlain.music.songsubdomain.datamapperlayer.SongRequestModelMapper;
import com.champlain.music.songsubdomain.datamapperlayer.SongResponseModelMapper;
import com.champlain.music.songsubdomain.presentationlayer.SongRequestModel;
import com.champlain.music.songsubdomain.presentationlayer.SongResponseModel;
import com.champlain.music.utils.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService{
    private final SongRepository songRepository;
    private final SongRequestModelMapper songRequestModelMapper;
    private final SongResponseModelMapper songResponseModelMapper;

    public SongServiceImpl(SongRepository songRepository, SongRequestModelMapper songRequestModelMapper, SongResponseModelMapper songResponseModelMapper) {
        this.songRepository = songRepository;
        this.songRequestModelMapper = songRequestModelMapper;
        this.songResponseModelMapper = songResponseModelMapper;
    }

    @Override
    public List<SongResponseModel> getAllSongs() {
        return songResponseModelMapper.entityToResponseModelList(songRepository.findAll());
    }

    @Override
    public SongResponseModel getSongById(String songId) {
        return songResponseModelMapper.entityToResponseModel(songRepository.findSongByIdentifier_SongId(songId));
    }

    @Override
    public SongResponseModel addSong(SongRequestModel songRequestModel) {
        Song song = songRequestModelMapper.requestModelToEntity(songRequestModel);
        song.setIdentifier(new SongIdentifier());
        return songResponseModelMapper.entityToResponseModel(songRepository.save(song));
    }

    @Override
    public SongResponseModel updateSong(SongRequestModel songRequestModel, String songId) {
        Song oldSong = songRepository.findSongByIdentifier_SongId(songId);
        if(oldSong != null) {
            Song song = songRequestModelMapper.requestModelToEntity(songRequestModel);
            song.setIdentifier(new SongIdentifier(songId));
            song.setId(oldSong.getId());
            return songResponseModelMapper.entityToResponseModel(songRepository.save(song));
        }
        else
            throw new NotFoundException("artist with id " + songId + " was not found");
    }

    @Override
    public void deleteSong(String songId) {
        Song song = songRepository.findSongByIdentifier_SongId(songId);
        if (song != null) {
            songRepository.delete(song);
        }
        else
            throw new NotFoundException("artist with id " + songId + " was not found");
    }
}
