package com.champlain.playlistservice.buisneelogiclayer;

import com.champlain.playlistservice.dataaccesslayer.playlist.Playlist;
import com.champlain.playlistservice.dataaccesslayer.playlist.PlaylistIdentifier;
import com.champlain.playlistservice.dataaccesslayer.playlist.PlaylistRepository;
import com.champlain.playlistservice.datamapperlayer.PlaylistRequestModelMapper;
import com.champlain.playlistservice.datamapperlayer.PlaylistResponseModelMapper;
import com.champlain.playlistservice.domainclientlayer.SongServiceClient;
import com.champlain.playlistservice.domainclientlayer.UserServiceClient;
import com.champlain.playlistservice.presentationlayer.ArtistResponseModel;
import com.champlain.playlistservice.presentationlayer.PlaylistRequestModel;
import com.champlain.playlistservice.presentationlayer.PlaylistResponseModel;
import com.champlain.playlistservice.presentationlayer.SongResponseModel;
import com.champlain.playlistservice.utils.exceptions.DuplicatePlaylistNameException;
import com.champlain.playlistservice.utils.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistRequestModelMapper playlistRequestModelMapper;
    private final PlaylistResponseModelMapper playlistResponseModelMapper;
    private final UserServiceClient userServiceClient;
    private final SongServiceClient songServiceClient;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository, PlaylistRequestModelMapper playlistRequestModelMapper, PlaylistResponseModelMapper playlistResponseModelMapper, UserServiceClient userServiceClient, SongServiceClient songServiceClient) {
        this.playlistRepository = playlistRepository;
        this.playlistRequestModelMapper = playlistRequestModelMapper;
        this.playlistResponseModelMapper = playlistResponseModelMapper;
        this.userServiceClient = userServiceClient;
        this.songServiceClient = songServiceClient;
    }

    @Override
    public List<PlaylistResponseModel> getAllPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        List<PlaylistResponseModel> playlistResponseModels = playlistResponseModelMapper.entityToResponseModelList(playlists);
        playlistResponseModels.forEach(playlist -> playlist.setSongs(
                playlists.stream()
                        .filter(p -> p.getIdentifier().getPlaylistId().equals(playlist.getIdentifier()))
                        .findAny()
                        .map(p -> p.getSongs().stream().map(songServiceClient::getSongById).toList())
                        .orElseGet(List::of)
        ));
        playlistResponseModels.forEach(playlist -> playlist.setUser(
                playlists.stream()
                        .filter(p -> p.getIdentifier().getPlaylistId().equals(playlist.getIdentifier()))
                        .findAny()
                        .map(p -> userServiceClient.getUserById(p.getUser()))
                        .orElse(null)
        ));
        return playlistResponseModels;
    }

    @Override
    public PlaylistResponseModel getPlaylistById(String playlistId) {
        Playlist playlist = playlistRepository.findByIdentifier_PlaylistId(playlistId);

        if (playlist == null)
            throw new NotFoundException("Playlist with id " + playlistId + " not found");

        PlaylistResponseModel playlistResponseModel = playlistResponseModelMapper.entityToResponseModel(playlist);
        addSongs(playlistResponseModel, playlist);
        addUser(playlistResponseModel, playlist);
        return playlistResponseModel;
    }

    @Override
    public PlaylistResponseModel addPlaylist(PlaylistRequestModel playlistRequestModel) {
        Playlist playlist = playlistRequestModelMapper.requestModelToEntity(playlistRequestModel);

        if(playlistRepository.existsByName(playlist.getName()))
            throw new DuplicatePlaylistNameException("Playlist name " + playlist.getName() + " already exists.");

        Duration duration = Duration.ZERO;
        for (String songIdentifier : playlist.getSongs()) {
            SongResponseModel song = songServiceClient.getSongById(songIdentifier);
            if (song == null)
                throw new NotFoundException("song with id " + songIdentifier + " was not found");

            LocalTime songTime = song.getDuration().toLocalTime();
            duration = duration.plus(Duration.ofHours(songTime.getHour()).plusMinutes(songTime.getMinute()).plusSeconds(songTime.getSecond()));
        }
        if (userServiceClient.getUserById(playlist.getUser()) == null)
            throw new NotFoundException("user with id " + playlist.getUser() + " was not found");
        playlist.setDuration(Time.valueOf(LocalTime.ofSecondOfDay(duration.getSeconds() % (24 * 60 * 60))));
        playlist.setIdentifier(new PlaylistIdentifier());
        PlaylistResponseModel playlistResponseModel = playlistResponseModelMapper.entityToResponseModel(playlistRepository.save(playlist));
        addSongs(playlistResponseModel, playlist);
        addUser(playlistResponseModel, playlist);
        return playlistResponseModel;
    }

    @Override
    public PlaylistResponseModel updatePlaylist(PlaylistRequestModel playlistRequestModel, String playlistId) {
        Playlist oldPlaylist = playlistRepository.findByIdentifier_PlaylistId(playlistId);
        if (oldPlaylist != null) {
            Playlist playlist = playlistRequestModelMapper.requestModelToEntity(playlistRequestModel);
            Duration duration = Duration.ZERO;
            for (String songIdentifier : playlist.getSongs()) {
                SongResponseModel song = songServiceClient.getSongById(songIdentifier);
                if (song == null)
                    throw new NotFoundException("song with id " + songIdentifier + " was not found");

                LocalTime songTime = song.getDuration().toLocalTime();
                duration = duration.plus(Duration.ofHours(songTime.getHour()).plusMinutes(songTime.getMinute()).plusSeconds(songTime.getSecond()));
            }
            if (userServiceClient.getUserById(playlist.getUser()) == null)
                throw new NotFoundException("user with id " + playlist.getUser() + " was not found");
            playlist.setDuration(Time.valueOf(LocalTime.ofSecondOfDay(duration.getSeconds() % (24 * 60 * 60))));
            playlist.setIdentifier(new PlaylistIdentifier(playlistId));
            playlist.setId(oldPlaylist.getId());
            PlaylistResponseModel playlistResponseModel = playlistResponseModelMapper.entityToResponseModel(playlistRepository.save(playlist));
            addSongs(playlistResponseModel, playlist);
            addUser(playlistResponseModel, playlist);
            return playlistResponseModel;
        } else
            throw new NotFoundException("playlist with id " + playlistId + " was not found");
    }

    @Override
    public void deletePlaylist(String playlistId) {
        Playlist playlist = playlistRepository.findByIdentifier_PlaylistId(playlistId);
        if (playlist != null) {
            playlistRepository.delete(playlist);
        } else
            throw new NotFoundException("playlist with id " + playlistId + " was not found");
    }

    @Override
    public List<ArtistResponseModel> getAllArtists(String playlistId) {
        return getPlaylistById(playlistId).getSongs().stream().map(SongResponseModel::getArtists).flatMap(List::stream).distinct().toList();
    }

    @Override
    public List<PlaylistResponseModel> getPlaylistsByUserId(String userId) {
        List<Playlist> playlists = playlistRepository.findAllByUser(userId);
        List<PlaylistResponseModel> playlistResponseModels = playlistResponseModelMapper.entityToResponseModelList(playlists);
        playlistResponseModels.forEach(playlist -> playlist.setSongs(
                playlists.stream()
                        .filter(p -> p.getIdentifier().getPlaylistId().equals(playlist.getIdentifier()))
                        .findAny()
                        .map(p -> p.getSongs().stream().map(songServiceClient::getSongById).toList())
                        .orElseGet(List::of)
        ));
        playlistResponseModels.forEach(playlist -> playlist.setUser(
                playlists.stream()
                        .filter(p -> p.getIdentifier().getPlaylistId().equals(playlist.getIdentifier()))
                        .findAny()
                        .map(p -> userServiceClient.getUserById(p.getUser()))
                        .orElse(null)
        ));
        return playlistResponseModels;
    }

    @Override @Transactional
    public void deletePlaylistsByUserId(String userId) {
        playlistRepository.deleteAllByUser(userId);
    }

    @Override
    public List<PlaylistResponseModel> getPlaylistsBySongId(String songId) {
        List<Playlist> playlists = playlistRepository.findAllBySongsContains(songId);
        List<PlaylistResponseModel> playlistResponseModels = playlistResponseModelMapper.entityToResponseModelList(playlists);
        playlistResponseModels.forEach(playlist -> playlist.setSongs(
                playlists.stream()
                        .filter(p -> p.getIdentifier().getPlaylistId().equals(playlist.getIdentifier()))
                        .findAny()
                        .map(p -> p.getSongs().stream().map(songServiceClient::getSongById).toList())
                        .orElseGet(List::of)
        ));
        playlistResponseModels.forEach(playlist -> playlist.setUser(
                playlists.stream()
                        .filter(p -> p.getIdentifier().getPlaylistId().equals(playlist.getIdentifier()))
                        .findAny()
                        .map(p -> userServiceClient.getUserById(p.getUser()))
                        .orElse(null)
        ));
        return playlistResponseModels;
    }

    public void addUser(PlaylistResponseModel playlistResponseModel, Playlist playlist) {
        playlistResponseModel.setUser(userServiceClient.getUserById(playlist.getUser()));
    }

    public void addSongs(PlaylistResponseModel playlistResponseModel, Playlist playlist) {
        playlistResponseModel.setSongs(playlist.getSongs().stream().map(songServiceClient::getSongById).toList());
    }
}
