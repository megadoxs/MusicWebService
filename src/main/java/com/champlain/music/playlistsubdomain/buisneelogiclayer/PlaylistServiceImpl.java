package com.champlain.music.playlistsubdomain.buisneelogiclayer;

import com.champlain.music.playlistsubdomain.dataaccesslayer.playlist.Playlist;
import com.champlain.music.playlistsubdomain.dataaccesslayer.playlist.PlaylistIdentifier;
import com.champlain.music.playlistsubdomain.dataaccesslayer.playlist.PlaylistRepository;
import com.champlain.music.playlistsubdomain.datamapperlayer.PlaylistRequestModelMapper;
import com.champlain.music.playlistsubdomain.datamapperlayer.PlaylistResponseModelMapper;
import com.champlain.music.playlistsubdomain.presentationlayer.ArtistResponseModel;
import com.champlain.music.playlistsubdomain.presentationlayer.PlaylistRequestModel;
import com.champlain.music.playlistsubdomain.presentationlayer.PlaylistResponseModel;
import com.champlain.music.playlistsubdomain.presentationlayer.SongResponseModel;
import com.champlain.music.utils.NotFoundException;
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
        PlaylistResponseModel playlistResponseModel = playlistResponseModelMapper.entityToResponseModel(playlist);
        addSongs(playlistResponseModel, playlist);
        addUser(playlistResponseModel, playlist);
        return playlistResponseModel;
    }

    @Override
    public PlaylistResponseModel addPlaylist(PlaylistRequestModel playlistRequestModel) {
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

    public void addUser(PlaylistResponseModel playlistResponseModel, Playlist playlist) {
        playlistResponseModel.setUser(userServiceClient.getUserById(playlist.getUser()));
    }

    public void addSongs(PlaylistResponseModel playlistResponseModel, Playlist playlist) {
        playlistResponseModel.setSongs(playlist.getSongs().stream().map(songServiceClient::getSongById).toList());
    }
}
